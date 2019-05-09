package xin.tongcangzhen.zhihufake.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.commons.lang.CharUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private TrieNode rootNode = new TrieNode();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            reader.close();
        } catch (Exception e) {
            logger.error("读取失败：" + e.getMessage());
        }
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        while (position < text.length()) {
            Character c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin));
                position=begin+1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isEnd()) {
                result.append(replacement);
                position++;
                begin = position;
            } else {
                position++;
            }
        }
        return result.toString();
    }

    private void addWord(String lineText) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineText.length() - 1) {
                tempNode.setEnd(true);
            }
        }

    }

    private class TrieNode {
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isEnd() {
            return end;
        }

        void setEnd(boolean end) {
            this.end = end;
        }
    }



//    public static void main(String[] args) {
//        SensitiveService s = new SensitiveService();
//        s.addWord("赌博");
//        s.addWord("贱人");
//        System.out.println(s.filter(" hi   你好哥哥贱 人呜呜呜赌博啊啊"));
//    }

}
