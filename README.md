## emojichinese简介

   一个把Emoji表情转换中文字符的工具包


## 使用方法
   1.初始化：
```xml
EmojiParser.getInstance().initConfig(context,EmojiParser.REGEX_STR_DEFAULT,EmojiParser.CHINESE_EMOJI_DEFAULT);
```
2.emoji表情转换成中文
```xml
String str=emojiParser.parseEmojiToChineseText(str);
```
3.中文转换emoji表情
```xml
SpannableStringBuilder spannableStringBuilder=emojiParser.getExpressionString(spannableString);
textView.setText(spannableStringBuilder);
```
