package com.vk.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiParser {
    static final String TAG = EmojiParser.class.getSimpleName();
    public static final String REGEX_STR_DEFAULT = "\\/(.*?)\\;";
    public static final String REGEX_STR_DEFAULT2 = "\\[(.*?)\\]";
    public static String CHINESE_EMOJI_DEFAULT = "chinese_emoji.xml";
    Context mContext;
    HashMap<List<Integer>, String> convertMap = new HashMap<>();
    HashMap<String, String> emojiToChineseMap = new HashMap<>();
    HashMap<String, String> chineseToEmojiMap = new HashMap<>();
    String regexStr;
    String emojiXml;
    String emojiRegexBegin, emojiRegexEnd;

    private EmojiParser() {
    }

    public void initConfig(Context context, String emojiRegex, String emojiXML) {
        this.mContext = context.getApplicationContext();
        this.regexStr = emojiRegex;
        this.emojiXml = emojiXML;
        readDataToMap(mContext);
        emojiRegexBegin = String.valueOf(emojiRegex.charAt(0) != '\\' ? emojiRegex.charAt(0) : emojiRegex.charAt(1)).intern();
        emojiRegexEnd = String.valueOf(emojiRegex.charAt(emojiRegex.length() - 1)).intern();
    }

    private static class Holder {
        private static EmojiParser instance = new EmojiParser();
    }


    public static EmojiParser getInstance() {
        return Holder.instance;
    }


    void readDataToMap(Context mContext) {
        if (emojiToChineseMap == null || emojiToChineseMap.size() == 0) {
            InputStream stream = null;
            try {
                XmlPullParser xmlpull = XmlPullParserFactory.newInstance().newPullParser();
                stream = mContext.getAssets().open(emojiXml);
                xmlpull.setInput(stream, "UTF-8");
                int eventCode = xmlpull.getEventType();
                while (eventCode != XmlPullParser.END_DOCUMENT) {
                    switch (eventCode) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (xmlpull.getName().equals("emoji")) {
                                String key = xmlpull.getAttributeValue(0);
                                String fromAttr = xmlpull.nextText();
                                List<Integer> fromCodePoints = new ArrayList<>();
                                if (xmlpull.getEventType() != XmlPullParser.END_TAG)
                                    xmlpull.nextTag();
                                if (!TextUtils.isEmpty(key)) {
                                    emojiToChineseMap.put(key + "", fromAttr + "");
                                    chineseToEmojiMap.put(fromAttr + "", key + "");
                                    fromCodePoints.add(Integer.parseInt(key, 16));
                                    convertMap.put(fromCodePoints, key + "");
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventCode = xmlpull.next();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    SpannableStringBuilder dealExpression(Context context, SpannableStringBuilder spannableString, Pattern patten, int start, int w, int h)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString.toString());
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start)
                continue;
            String pattern = patten.pattern().intern();
            if (pattern == null || pattern.length() < 3)
                return spannableString;
            key = key.substring(key.indexOf(emojiRegexBegin) + 1, key.lastIndexOf(emojiRegexEnd));
            String emoId;
            if (chineseToEmojiMap.containsKey(key))
                emoId = chineseToEmojiMap.get(key);
            else
                emoId = key;
            int resId = Integer.parseInt(R.drawable.class.getDeclaredField("emoji_" + emoId).get(null).toString());
            if (resId != 0) {
                Drawable d = context.getResources().getDrawable(resId);
                if (d != null) {
                    if (w == 0 && h == 0)
                        d.setBounds(0, 0, d.getIntrinsicWidth() * 3 / 3,
                                d.getIntrinsicHeight() * 3 / 3);
                    else
                        d.setBounds(0, 0, w, h);
                    spannableString.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BOTTOM), matcher.start(), matcher.end(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }

            }
        }
        return spannableString;
    }


    public SpannableStringBuilder getExpressionString(SpannableStringBuilder spannableString, int w, int h) {
        try {
            return dealExpression(mContext, spannableString, Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE), 0, w, h);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return spannableString;
    }

    public SpannableStringBuilder getExpressionString(SpannableStringBuilder spannableString) {

        return getExpressionString(spannableString, 0, 0);
    }

    public SpannableStringBuilder getExpressionString(CharSequence cs) {

        return getExpressionString(new SpannableStringBuilder(cs));
    }


    public String parseEmojiToChineseText(String input) {
        if (input == null || input.length() <= 0)
            return "";
        StringBuilder result = new StringBuilder();
        int[] codePoints = toCodePointArray(input);
        List<Integer> key = null;
        for (int i = 0; i < codePoints.length; i++) {
            key = new ArrayList<>();
            if (i + 1 < codePoints.length) {
                key.add(codePoints[i]);
                key.add(codePoints[i + 1]);
                if (convertMap.containsKey(key)) {
                    String value = convertMap.get(key);
                    if (value != null) {
                        if (emojiToChineseMap.containsKey(value)) {
                            result.append(emojiRegexBegin + emojiToChineseMap.get(value) + emojiRegexEnd);
                        } else {
                            result.append(emojiRegexBegin + value + emojiRegexEnd);
                        }

                    }
                    i++;
                    continue;
                }
            }
            key.clear();
            key.add(codePoints[i]);
            if (convertMap.containsKey(key)) {
                String value = convertMap.get(key);
                if (value != null) {
                    // result.append("[e]" + value + "[/e]");
                    if (emojiToChineseMap.containsKey(value)) {
                        result.append(emojiRegexBegin+ emojiToChineseMap.get(value) + emojiRegexEnd);
                    } else {
                        result.append(emojiRegexBegin + value + emojiRegexEnd);
                    }
                }
                continue;
            }
            result.append(Character.toChars(codePoints[i]));
        }
        return result.toString();
    }

    int[] toCodePointArray(String str) {
        char[] ach = str.toCharArray();
        int len = ach.length;
        int[] acp = new int[Character.codePointCount(ach, 0, len)];
        int j = 0;
        for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(ach, i);
            acp[j++] = cp;
        }
        return acp;
    }


}
