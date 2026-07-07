package CodePilotAI.config;

import CodePilotAI.entity.Question;
import CodePilotAI.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

// Seeds a broad LeetCode-style question bank on first run so Practice
// Questions isn't empty for a brand-new database. Safe to re-run: it
// only inserts when the table is empty.
@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedQuestions(QuestionRepository questionRepository) {
        return args -> {
            if (questionRepository.count() > 0) {
                return;
            }
            List<Question> questions = buildQuestions();
            assignCompanies(questions);
            questionRepository.saveAll(questions);
        };
    }

    // Realistic-ish company tagging by topic, since real "which company asks this"
    // data isn't something this project scrapes — this keeps the company filter
    // functional and demonstrable without claiming to be a verified dataset.
    private void assignCompanies(List<Question> questions) {
        java.util.Map<String, String> companiesByTopic = java.util.Map.ofEntries(
                java.util.Map.entry("Arrays", "Amazon,Google,Meta,Microsoft"),
                java.util.Map.entry("Strings", "Meta,Microsoft,Apple,Adobe"),
                java.util.Map.entry("Linked List", "Amazon,Microsoft,Oracle"),
                java.util.Map.entry("Trees", "Microsoft,Amazon,Adobe,Bloomberg"),
                java.util.Map.entry("Graphs", "Google,Uber,Meta,Netflix"),
                java.util.Map.entry("DP", "Google,Amazon,Bloomberg,Apple"),
                java.util.Map.entry("Stack", "Google,Adobe,Uber"),
                java.util.Map.entry("Heap", "Amazon,Uber,Netflix"),
                java.util.Map.entry("Searching", "Google,Oracle,Microsoft"),
                java.util.Map.entry("Bit Manipulation", "Apple,Oracle,Adobe")
        );
        for (Question question : questions) {
            question.setCompanies(companiesByTopic.getOrDefault(question.getTopic(), "Amazon,Google"));
        }
    }

    private List<Question> buildQuestions() {
        return List.of(
            q("Two Sum", "Arrays", "Easy",
                "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume each input has exactly one solution, and you may not use the same element twice.",
                "Input: nums = [2,7,11,15], target = 9\nOutput: [0,1]\nExplanation: nums[0] + nums[1] == 9",
                "2 <= nums.length <= 10^4 · -10^9 <= nums[i] <= 10^9",
                "https://leetcode.com/problems/two-sum/"),

            q("Best Time to Buy and Sell Stock", "Arrays", "Easy",
                "You are given an array prices where prices[i] is the price of a stock on day i. Find the maximum profit from choosing a single day to buy and a later day to sell.",
                "Input: prices = [7,1,5,3,6,4]\nOutput: 5\nExplanation: Buy on day 2 (price=1), sell on day 5 (price=6), profit = 5.",
                "1 <= prices.length <= 10^5",
                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/"),

            q("Contains Duplicate", "Arrays", "Easy",
                "Given an integer array nums, return true if any value appears at least twice in the array.",
                "Input: nums = [1,2,3,1]\nOutput: true",
                "1 <= nums.length <= 10^5",
                "https://leetcode.com/problems/contains-duplicate/"),

            q("Product of Array Except Self", "Arrays", "Medium",
                "Given an integer array nums, return an array answer such that answer[i] is equal to the product of all elements of nums except nums[i], without using division.",
                "Input: nums = [1,2,3,4]\nOutput: [24,12,8,6]",
                "2 <= nums.length <= 10^5",
                "https://leetcode.com/problems/product-of-array-except-self/"),

            q("Maximum Subarray", "Arrays", "Medium",
                "Given an integer array nums, find the contiguous subarray with the largest sum and return its sum (Kadane's algorithm).",
                "Input: nums = [-2,1,-3,4,-1,2,1,-5,4]\nOutput: 6\nExplanation: [4,-1,2,1] has the largest sum = 6.",
                "1 <= nums.length <= 10^5",
                "https://leetcode.com/problems/maximum-subarray/"),

            q("Merge Intervals", "Arrays", "Medium",
                "Given an array of intervals, merge all overlapping intervals and return an array of the non-overlapping intervals.",
                "Input: intervals = [[1,3],[2,6],[8,10],[15,18]]\nOutput: [[1,6],[8,10],[15,18]]",
                "1 <= intervals.length <= 10^4",
                "https://leetcode.com/problems/merge-intervals/"),

            q("3Sum", "Arrays", "Medium",
                "Given an integer array nums, return all unique triplets [nums[i], nums[j], nums[k]] such that they add up to 0.",
                "Input: nums = [-1,0,1,2,-1,-4]\nOutput: [[-1,-1,2],[-1,0,1]]",
                "3 <= nums.length <= 3000",
                "https://leetcode.com/problems/3sum/"),

            q("Trapping Rain Water", "Arrays", "Hard",
                "Given n non-negative integers representing an elevation map, compute how much water it can trap after raining.",
                "Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]\nOutput: 6",
                "1 <= height.length <= 2*10^4",
                "https://leetcode.com/problems/trapping-rain-water/"),

            q("Valid Anagram", "Strings", "Easy",
                "Given two strings s and t, return true if t is an anagram of s.",
                "Input: s = \"anagram\", t = \"nagaram\"\nOutput: true",
                "1 <= s.length, t.length <= 5*10^4",
                "https://leetcode.com/problems/valid-anagram/"),

            q("Valid Palindrome", "Strings", "Easy",
                "Given a string s, return true if it is a palindrome after converting to lowercase and removing non-alphanumeric characters.",
                "Input: s = \"A man, a plan, a canal: Panama\"\nOutput: true",
                "1 <= s.length <= 2*10^5",
                "https://leetcode.com/problems/valid-palindrome/"),

            q("Longest Substring Without Repeating Characters", "Strings", "Medium",
                "Given a string s, find the length of the longest substring without repeating characters (sliding window).",
                "Input: s = \"abcabcbb\"\nOutput: 3\nExplanation: \"abc\" has length 3.",
                "0 <= s.length <= 5*10^4",
                "https://leetcode.com/problems/longest-substring-without-repeating-characters/"),

            q("Group Anagrams", "Strings", "Medium",
                "Given an array of strings, group the anagrams together.",
                "Input: strs = [\"eat\",\"tea\",\"tan\",\"ate\",\"nat\",\"bat\"]\nOutput: [[\"bat\"],[\"nat\",\"tan\"],[\"ate\",\"eat\",\"tea\"]]",
                "1 <= strs.length <= 10^4",
                "https://leetcode.com/problems/group-anagrams/"),

            q("Longest Palindromic Substring", "Strings", "Medium",
                "Given a string s, return the longest palindromic substring in s.",
                "Input: s = \"babad\"\nOutput: \"bab\" (or \"aba\")",
                "1 <= s.length <= 1000",
                "https://leetcode.com/problems/longest-palindromic-substring/"),

            q("Minimum Window Substring", "Strings", "Hard",
                "Given strings s and t, return the minimum window substring of s such that every character in t is included.",
                "Input: s = \"ADOBECODEBANC\", t = \"ABC\"\nOutput: \"BANC\"",
                "1 <= s.length, t.length <= 10^5",
                "https://leetcode.com/problems/minimum-window-substring/"),

            q("Reverse Linked List", "Linked List", "Easy",
                "Given the head of a singly linked list, reverse the list and return the new head.",
                "Input: head = [1,2,3,4,5]\nOutput: [5,4,3,2,1]",
                "0 <= number of nodes <= 5000",
                "https://leetcode.com/problems/reverse-linked-list/"),

            q("Merge Two Sorted Lists", "Linked List", "Easy",
                "Merge two sorted linked lists and return it as a new sorted list.",
                "Input: list1 = [1,2,4], list2 = [1,3,4]\nOutput: [1,1,2,3,4,4]",
                "0 <= number of nodes in each list <= 50",
                "https://leetcode.com/problems/merge-two-sorted-lists/"),

            q("Linked List Cycle", "Linked List", "Easy",
                "Given the head of a linked list, determine if the linked list has a cycle (Floyd's algorithm).",
                "Input: head = [3,2,0,-4], pos = 1\nOutput: true",
                "0 <= number of nodes <= 10^4",
                "https://leetcode.com/problems/linked-list-cycle/"),

            q("Remove Nth Node From End of List", "Linked List", "Medium",
                "Given the head of a linked list, remove the nth node from the end and return its head.",
                "Input: head = [1,2,3,4,5], n = 2\nOutput: [1,2,3,5]",
                "1 <= number of nodes <= 30",
                "https://leetcode.com/problems/remove-nth-node-from-end-of-list/"),

            q("Binary Tree Level Order Traversal", "Trees", "Medium",
                "Given the root of a binary tree, return the level order traversal of its nodes' values.",
                "Input: root = [3,9,20,null,null,15,7]\nOutput: [[3],[9,20],[15,7]]",
                "0 <= number of nodes <= 2000",
                "https://leetcode.com/problems/binary-tree-level-order-traversal/"),

            q("Validate Binary Search Tree", "Trees", "Medium",
                "Given the root of a binary tree, determine if it is a valid binary search tree.",
                "Input: root = [2,1,3]\nOutput: true",
                "1 <= number of nodes <= 10^4",
                "https://leetcode.com/problems/validate-binary-search-tree/"),

            q("Maximum Depth of Binary Tree", "Trees", "Easy",
                "Given the root of a binary tree, return its maximum depth.",
                "Input: root = [3,9,20,null,null,15,7]\nOutput: 3",
                "0 <= number of nodes <= 10^4",
                "https://leetcode.com/problems/maximum-depth-of-binary-tree/"),

            q("Lowest Common Ancestor of a BST", "Trees", "Medium",
                "Given a BST, find the lowest common ancestor of two given nodes.",
                "Input: root = [6,2,8,0,4,7,9], p = 2, q = 8\nOutput: 6",
                "2 <= number of nodes <= 10^5",
                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/"),

            q("Serialize and Deserialize Binary Tree", "Trees", "Hard",
                "Design an algorithm to serialize and deserialize a binary tree.",
                "Input: root = [1,2,3,null,null,4,5]\nOutput: [1,2,3,null,null,4,5]",
                "0 <= number of nodes <= 10^4",
                "https://leetcode.com/problems/serialize-and-deserialize-binary-tree/"),

            q("Number of Islands", "Graphs", "Medium",
                "Given an m x n 2D grid of '1's (land) and '0's (water), return the number of islands.",
                "Input: grid = [[\"1\",\"1\",\"0\"],[\"1\",\"0\",\"0\"],[\"0\",\"0\",\"1\"]]\nOutput: 2",
                "1 <= m, n <= 300",
                "https://leetcode.com/problems/number-of-islands/"),

            q("Course Schedule", "Graphs", "Medium",
                "Given numCourses and prerequisite pairs, determine if you can finish all courses (cycle detection).",
                "Input: numCourses = 2, prerequisites = [[1,0]]\nOutput: true",
                "1 <= numCourses <= 2000",
                "https://leetcode.com/problems/course-schedule/"),

            q("Clone Graph", "Graphs", "Medium",
                "Given a reference of a node in a connected undirected graph, return a deep copy of the graph.",
                "Input: adjList = [[2,4],[1,3],[2,4],[1,3]]\nOutput: [[2,4],[1,3],[2,4],[1,3]]",
                "0 <= number of nodes <= 100",
                "https://leetcode.com/problems/clone-graph/"),

            q("Pacific Atlantic Water Flow", "Graphs", "Medium",
                "Given an m x n matrix of heights, find cells where water can flow to both the Pacific and Atlantic oceans.",
                "Input: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]",
                "1 <= m, n <= 200",
                "https://leetcode.com/problems/pacific-atlantic-water-flow/"),

            q("Word Ladder", "Graphs", "Hard",
                "Given beginWord, endWord, and a wordList, return the length of the shortest transformation sequence (BFS).",
                "Input: beginWord = \"hit\", endWord = \"cog\", wordList = [\"hot\",\"dot\",\"dog\",\"lot\",\"log\",\"cog\"]\nOutput: 5",
                "1 <= wordList.length <= 5000",
                "https://leetcode.com/problems/word-ladder/"),

            q("Climbing Stairs", "DP", "Easy",
                "You are climbing a staircase of n steps, taking 1 or 2 steps at a time. Count the distinct ways to reach the top.",
                "Input: n = 3\nOutput: 3\nExplanation: 1+1+1, 1+2, 2+1",
                "1 <= n <= 45",
                "https://leetcode.com/problems/climbing-stairs/"),

            q("House Robber", "DP", "Medium",
                "Given an array of house values, determine the maximum amount you can rob without robbing two adjacent houses.",
                "Input: nums = [1,2,3,1]\nOutput: 4",
                "1 <= nums.length <= 100",
                "https://leetcode.com/problems/house-robber/"),

            q("Longest Common Subsequence", "DP", "Medium",
                "Given two strings, return the length of their longest common subsequence.",
                "Input: text1 = \"abcde\", text2 = \"ace\"\nOutput: 3",
                "1 <= text1.length, text2.length <= 1000",
                "https://leetcode.com/problems/longest-common-subsequence/"),

            q("Coin Change", "DP", "Medium",
                "Given coin denominations and a target amount, return the fewest number of coins needed to make that amount.",
                "Input: coins = [1,2,5], amount = 11\nOutput: 3\nExplanation: 5+5+1",
                "1 <= coins.length <= 12",
                "https://leetcode.com/problems/coin-change/"),

            q("Word Break", "DP", "Medium",
                "Given a string s and a dictionary of words, determine if s can be segmented into space-separated dictionary words.",
                "Input: s = \"leetcode\", wordDict = [\"leet\",\"code\"]\nOutput: true",
                "1 <= s.length <= 300",
                "https://leetcode.com/problems/word-break/"),

            q("0/1 Knapsack", "DP", "Hard",
                "Given weights and values of items and a knapsack capacity, maximize total value without exceeding capacity.",
                "Input: weights=[1,3,4,5], values=[1,4,5,7], capacity=7\nOutput: 9",
                "1 <= n <= 1000",
                "https://www.geeksforgeeks.org/0-1-knapsack-problem-dp-10/"),

            q("Edit Distance", "DP", "Hard",
                "Given two strings, return the minimum number of operations (insert/delete/replace) to convert one to the other.",
                "Input: word1 = \"horse\", word2 = \"ros\"\nOutput: 3",
                "0 <= word1.length, word2.length <= 500",
                "https://leetcode.com/problems/edit-distance/"),

            q("Valid Parentheses", "Stack", "Easy",
                "Given a string containing just brackets, determine if the input string is valid (properly matched/nested).",
                "Input: s = \"()[]{}\"\nOutput: true",
                "1 <= s.length <= 10^4",
                "https://leetcode.com/problems/valid-parentheses/"),

            q("Min Stack", "Stack", "Medium",
                "Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.",
                "Input: push(-2), push(0), push(-3), getMin() -> -3, pop(), top() -> 0, getMin() -> -2",
                "-2^31 <= val <= 2^31 - 1",
                "https://leetcode.com/problems/min-stack/"),

            q("Daily Temperatures", "Stack", "Medium",
                "Given daily temperatures, return an array where answer[i] is how many days until a warmer temperature.",
                "Input: temperatures = [73,74,75,71,69,72,76,73]\nOutput: [1,1,4,2,1,1,0,0]",
                "1 <= temperatures.length <= 10^5",
                "https://leetcode.com/problems/daily-temperatures/"),

            q("Kth Largest Element in an Array", "Heap", "Medium",
                "Given an integer array and k, return the kth largest element in the array.",
                "Input: nums = [3,2,1,5,6,4], k = 2\nOutput: 5",
                "1 <= k <= nums.length <= 10^5",
                "https://leetcode.com/problems/kth-largest-element-in-an-array/"),

            q("Top K Frequent Elements", "Heap", "Medium",
                "Given an integer array and k, return the k most frequent elements.",
                "Input: nums = [1,1,1,2,2,3], k = 2\nOutput: [1,2]",
                "1 <= nums.length <= 10^5",
                "https://leetcode.com/problems/top-k-frequent-elements/"),

            q("Binary Search", "Searching", "Easy",
                "Given a sorted array of integers and a target, return the index of target if found, else -1, in O(log n).",
                "Input: nums = [-1,0,3,5,9,12], target = 9\nOutput: 4",
                "1 <= nums.length <= 10^4",
                "https://leetcode.com/problems/binary-search/"),

            q("Search in Rotated Sorted Array", "Searching", "Medium",
                "Given a rotated sorted array and a target, return its index, or -1 if not found (modified binary search).",
                "Input: nums = [4,5,6,7,0,1,2], target = 0\nOutput: 4",
                "1 <= nums.length <= 5000",
                "https://leetcode.com/problems/search-in-rotated-sorted-array/"),

            q("Single Number", "Bit Manipulation", "Easy",
                "Given a non-empty array where every element appears twice except for one, find that single one (XOR trick).",
                "Input: nums = [4,1,2,1,2]\nOutput: 4",
                "1 <= nums.length <= 3*10^4",
                "https://leetcode.com/problems/single-number/"),

            q("Number of 1 Bits", "Bit Manipulation", "Easy",
                "Write a function that returns the number of set bits (1s) in the binary representation of an integer.",
                "Input: n = 11 (binary 1011)\nOutput: 3",
                "n is a 32-bit unsigned integer",
                "https://leetcode.com/problems/number-of-1-bits/")
        );
    }

    private Question q(String title, String topic, String difficulty, String description,
                        String examples, String constraints, String link) {
        Question question = new Question();
        question.setTitle(title);
        question.setTopic(topic);
        question.setDifficulty(difficulty);
        question.setDescription(description);
        question.setExamples(examples);
        question.setConstraints(constraints);
        question.setLink(link);
        return question;
    }
}
