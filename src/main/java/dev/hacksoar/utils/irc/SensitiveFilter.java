package dev.hacksoar.utils.irc;

import dev.hacksoar.HackSoar;
import dev.hacksoar.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SensitiveFilter {
    /**
     * 替换符
     */
    private static final String REPLACEMENT = "***";

    /**
     * 根节点
     */
    private static final TrieNode rootNode = new TrieNode();

    public static final ArrayList<String> sensitiveWordsList = new ArrayList<>();

    public SensitiveFilter() {
        try {
            IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            ResourceLocation filePath = new ResourceLocation("hacksoar/sensitive-words.txt");
            InputStream inputStream = resourceManager.getResource(filePath).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                addKeyword(keyword);
                sensitiveWordsList.add(keyword);
            }
        } catch (IOException e) {
            if (HackSoar.instance.DEVELOPMENT_SWITCH) {
                Logger.error("Load SensitiveFilter error " + e.getMessage());
            }
        }
    }

    public ArrayList<String> getSensitiveWords() {
        return sensitiveWordsList;
    }

    /**
     * 将一个敏感词添加到前缀树中
     */
    private static void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 指向子节点,进入下一轮循环
            tempNode = subNode;
            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本。如果源字符串是null或者为空，返回 null
     */
    public String filter(String text) {
        if (text == null || "".equals(text.trim())) {
            return null;
        }
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);
            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }
            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     */
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private boolean isAsciiAlphanumeric(Character ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }

    /**
     * 前缀树
     */
    private static class TrieNode {
        /**
         * 关键词结束标识
         */
        private boolean isKeywordEnd = false;

        /**
         * 孩子节点(key是下级字符,value是下级节点)
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        /**
         * 添加子节点
         */
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }
}