# shrink-jar 简介
如果你使用java写了一个程序，并把所有class打包成一个可执行的jar文件，由于依赖了很多第三方库，这个jar文件会很大。但是第三方库并不是所有的class都会被用到，使用[shrink.jar](https://github.com/atomiCat/shrink-jar/raw/master/distribution/shrink.jar) 来删除用不到的class，减少你的可执行jar的体积。  
**shrink.jar** 是一个可执行jar，你可以使用命令` java -jar shrink.jar jar文件 classList文件`来执行。  
其中的参数：  
 `jar文件` 是你要精简的jar文件路径  
 `classList文件` 是一个txt文件，储存了需要保留的class列表。 
 # 使用方法
 以windows平台为例，假如要精简 **helloWorld.jar**:  
 1. 找一个空文件夹，将**shrink.jar**和**helloWorld.jar**放到该文件夹。  
 2. 执行  `java -verbose:class -jar helloWorld.jar > classList.txt` 来生成**classList.txt**，
 注意这一步要把**helloWorld.jar**的所有功能都执行一遍，以保证classList中包含了所有会用到的class，
 如果有测试用例最好运行一次测试用例。
 3. 执行 `java -jar shrink.jar helloWorld.jar classList.txt`  会生成 **helloWorld_mini.jar**文件，即为精简后的jar
 
