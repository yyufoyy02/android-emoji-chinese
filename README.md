## emojichinese简介

   一个可以把Emoji表情转换成中文编码的工具包


## 使用方法
   1.初始化：
```xml
EmojiParser.getInstance().initConfig(context,EmojiParser.REGEX_STR_DEFAULT,EmojiParser.CHINESE_EMOJI_DEFAULT);
```
```xml
EmojiParser.REGEX_STR_DEFAULT：表情中文解析正则表达式，默认"\/(.*?)\;"或"\[(.*?)\]"
EmojiParser.CHINESE_EMOJI_DEFAULT:emoji编码中文对应表，默认assets/chinese_emoji.xml,格式<emoji name="1f604">开心</emoji>
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
![](https://github.com/yyufoyy02/android-emoji-chinese/blob/master/Screenshot_2016-05-05-20-44-35.png)
![](https://github.com/yyufoyy02/android-emoji-chinese/blob/master/Screenshot_2016-05-05-20-44-35.png)

##开源协议
```
 Copyright (C) 2014, 梁伟健
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ```