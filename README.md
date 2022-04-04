# Hello Wordle

> 整理自 JEP cofe 10

此项目通过使用组合思想、stream流(8)、switch表达式(14)、record记录类(16)、sealed密封类(17)、switch模式匹配(preview)，来解决 Wordle Kata （即，一个叫 Wordle 编程挑战）。

## Wordle Kata 猜词游戏

有一个隐藏的（hidden）单词，长度是 5 个字母。你需要猜出这个词，然后你每次给出一个词，游戏会告诉你每个字母是否在隐藏词中，以及是否在正确的位置。

比如，隐藏单词是 `siren`，你猜的单词是 `sigma`，游戏就会告诉你 `SI...`，意思是 `s` 和 `i` 在正确的位置上，其它字母都不在隐藏词中。
同样，如果你猜的单词是 `sepia`，游戏会告诉你 `Se.i.`，意思是 `s` 在正确的位置上，而且 `e` 和 `i` 在隐藏词中存在，只是位置不对。
如果你猜 `agree`，游戏会告诉你 `..RE.`，意思是 `r` 和 第一个 `e` 在正确的位置上，其它字母都不在隐藏词中。注意：在正确位置上的字母优先考虑，然后由于隐藏单词中没有第二个 `e`，所以第二个 `e` 不被标记。
如果你猜 `siege`，游戏会告诉你 `SIe..`，在这里第一个 `e` 被标记“字母存在，但位置错误”，而对于第二个 `e`，因为隐藏词中没有第二个 `e`，就不会被标记提示。

再比如，隐藏词是 `siege`，你猜 `jewel`，游戏会提示 `.e.e.`。你猜 `agree`，游戏会提示 `.g.ee`。

## 尝试实现

本程序给出了四种实现：
* 使用 `org.eclipse.collections:eclipse-collections:11.0.0` 中的 `MutableCharBag`，实现。
* 使用 JDK 8 之前的技术实现
* 使用 JDK 8 的技术实现：主要是 stream 流
* 使用 JDK 17 的技术实现：stream流(8)、switch表达式(14)、record记录类(16)、sealed密封类(17)、switch模式匹配(preview)

