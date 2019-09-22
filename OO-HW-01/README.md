# OO homework 1：简单多项式导函数求解

## 问题描述

本次作业，需要完成的任务为**简单多项式导函数**的求解。问题具体内容在此不再赘述。



## 程序使用方法

本次作业源代码为两个`.java`文件：

- `Polynomial.java`
- `StringProcessor.java`

使用合适的 **JAVA IDE** 建立项目文件。下载上述两个`.java`文件。将其放置在项目的  src  文件夹下。切换到`Polynomial.java`文件下，右键点击“运行`Polynomial.java`”，即可运行。之后在控制台输入想待求导函数的简单多项式，键入`enter`可在控制台得到处理结果。



## 思路简介

程序的整个处理流程大致描述如下：

1.使用`Scanner`对象读入表达式字符串，建立`Polynomial`对象`poly`，用来存储输入的多项式；

```java
Polynomial poly = new Polynomial();
```

2.根据题目标准，建立规范多项式的正则表达式`termreg`，并调用 `java.util` 中的`Pattern`。并建立用来匹配`termreg`的`matcher0`；

```java
r = Pattern.compile(termreg);
matcher0 = r.matcher(str);
```

3.匹配当前输入的字符串，如果是合法字符串则进行下面的操作；否则输出错误提示语句，程序结束。

4.读取输入字符串，将其中的系数和次数对应地提取出来形成二元映射，并进行同类项合并。将处理的结果放在；

5.调用`poly`中有关于求导的方法，主要根据上述建立的二元映射进行求导，产生一个新的导函数`Polynomial`对象`derivation`。并调用输出方法将最短形式的多项式输出。



## 特殊的对象的作用

在作业的过程中， JAVA 自带工具包中几个强有力的工具大大简化了我们的实现方案。在此部分将统一介绍。

- `java.util.Pattern`和`java.util.matcher`

  主要用于**正则表达式**判断字符串合法。为了更加方便的判断表达式的合法性，引入了`Pattern`和`matcher`两个对象。`Pattern`主要用来捕捉要判断的正则表达式的特征，`matcher`用来匹配当前的字符串。使用的几个方法的作用如下：

  ​	`Pattern.matcher(String s)` ： 产生匹配字符串`s`的`matcher`。

  ​	`matcher.find()` ： 用来进行一次匹配。

  ​	`matcher.start()` ： 上次匹配的起始位置。

  ​	`matcher.end()` ： 下次匹配的开始位置。

  注意：

  1. 事实上，一开始简单的将整个字符串作为正则表达式进行匹配是有出问题的风险的。其主要在字符串太长一次匹配复杂度太高上面。在测试部分我们会详细说明针对这个问题的**处理办法**。
  2. 经过查阅资料知道正则表达式匹配有两种模式，一种是贪婪模式，一种是懒惰模式。这里统一选用贪婪模式。

- `java.math.BigInteger`

  为了防止`long`型对于本题中的数据溢出的情况，使用了 JAVA 在大数存储和运算中自带的包`BigInteger`。根据资料，这个对象的实现原理是将数转换成为字符串，所以一般说来不会存在溢出的情况，这为完成此次作业提供了很大的便利。使用的方法主要是运算、产生和比较功能的，在此不再赘述。

- `java.util.Hashmap`

  为了建立次数与系数的二值映射，对每一个`Polynomial`对象都有使用`Hashmap`容器建立的`factorandpower对象`。其key为变量次数，value为变量系数：

  ```java
  private HashMap<BigInteger,BigInteger> factorandpower = new HashMap<>();
  ```

  使用`Hashmap`的主要好处是：

  1. 易于插入、删除、修改：调用`factorandpower.put()`、`factorandpower.remove()`即可。
  2. 易于索引：找到 key 值直接调用`factorandpower.get(key)`

使用以上自带的特殊方法大大减少了我们的工作量，使整个程序更为简洁。



## 方法（函数）说明

### Polynomial对象

​    1.`void insertTerm(BigInteger pow,BigInteger coeff)` 

​	    输入当前得到的次数和系数，将他们插入本多项式的`factorandpower`中。

​    2. `void constructPoly(String s)` 

​	    通过 s 调用`StringProcess`中的`controlProcess`方法，产生多项式次数-系数的Hashmap。

​    3. `Polynomial takeDerivation()`

​	    对已经处理好的多项式求导，返回一个新的导函数`Polynomial`对象。

​    4.`void printPower(BigInteger power)`

​    	负责输出变量和系数部分。

​    5. `void showPoly()`

​	    调用`printPower`输出当前此对象存储的表达式。

​    6.`boolean judgePoly(String s)`

​	    判断当前输入的字符串`s`是否是一个合法的多项式。主要使用`pattern`和`matcher`进行判断。

​    7.`static void main(String[] args)`

​    	程序入口。

### StringProcess对象

​    1.`void skipBlank()`

​	    跳过字符串中的`<space>`和`\t`字符。

​    2.`void processSign()`

​    	处理系数符号或者加减符号。

​    3.`BigInteger processInteger()`

​    	处理当前位置的字符串并返回一个整数，直到读到的下一个字符不是`0-9`。

​    4.`BigInteger processPower()`

​	    读取乘幂函数部分。

​    5.`void controlProcess()`

​    	 控制器，控制整个字符串处理过程。



## 测试情况

​	通过了课程组给出的弱测和中测，但是在自己测试的时候发现了问题：在项数飙升到1000项左右的时候，无法进行字符串的匹配，理由是 StackOverflow 。

​	经过查资料发现`match`的匹配原理是递归匹配的，如果当做一整个字符串进行匹配，假设有1000项，在重复的时候就会递归 1000^1000  。

​	因此我们将一个项作为一个最小单位进行匹配。每次检查当前匹配上的项位置是不是上一个匹配项的结束为止，以此类推，直到匹配结束。并检查匹配结束时是否到达字符串的末尾。使用这样的方法我们的复杂度就大大降低了。

​	应该注意到的是：由于第一项很特殊，他前面可以没有符号，统一处理起来有困难。解决方法是先读前面的空格，直到读到不为空格的位置。如果这个位置不是符号，那么说明第一项没有符号，就给这个字符串前面加上一个正符号。然后再将多项式按照统一处理。