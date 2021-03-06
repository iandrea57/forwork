#### 参数

* $0 ：即命令本身，相当于c/c++中的argv[0]
* $1 ：第一个参数.
* $2, $3, $4 ... ：第2、3、4个参数，依次类推。
* $#  参数的个数，不包括命令本身
* $@ ：参数本身的列表，也不包括命令本身
* $* ：和$@相同，但"$*" 和 "$@"(加引号)并不同，"$*"将所有的参数解释成一个字符串，而"$@"是一个参数数组。


#### 基础

    `linux cmd` 可以用 $(linux cmd) 表示

#### 数组
    a=(1 2 3 4 5)
    ${#a[@]} 或 ${#a[*]}   获取长度
    ${a[@]:1:4}            子数组
    ${a[@]}                数组元素列表


#### 条件表达式

    if [ $# -lt 1];then
        echo -e "example:\ncmd $first $start $end"
        exit 1
    fi

    [ expression ]   以-gt -lt -o(or) -a(and)
    [[ expression ]] 可以 <  >  ||  &&

#### 运算

    ((i=$j+$k))
    i=`expr $j + $k`


#### 循环

    for((i=1;i<=10;i++))
    do
        echo $(expr $i \* 4)
    done

    for i in {1..10}
    for i in ${arr[@]}
    for i in $(seq 10)
    for i in `ls`
    for i in $(ls *.txt)



#### 运算符

###### 逻辑卷标

1. 关于档案与目录的侦测逻辑卷标！

    -f	常用！侦测『档案』是否存在 eg: if [ -f filename ]

    -d	常用！侦测『目录』是否存在
   
    -b	侦测是否为一个『 block 档案』

    -c	侦测是否为一个『 character 档案』

    -S	侦测是否为一个『 socket 标签档案』

    -L	侦测是否为一个『 symbolic link 的档案』

    -e	侦测『某个东西』是否存在！

2. 关于程序的逻辑卷标！

    -G	侦测是否由 GID 所执行的程序所拥有

    -O	侦测是否由 UID 所执行的程序所拥有

    -p	侦测是否为程序间传送信息的 name pipe 或是 FIFO （老实说，这个不太懂！）

3. 关于档案的属性侦测！

    -r	侦测是否为可读的属性

    -w	侦测是否为可以写入的属性

    -x	侦测是否为可执行的属性

    -s	侦测是否为『非空白档案』

    -u	侦测是否具有『 SUID 』的属性

    -g	侦测是否具有『 SGID 』的属性

    -k	侦测是否具有『 sticky bit 』的属性

4. 两个档案之间的判断与比较 ；例如[ test file1 -nt file2 ]

    -nt	第一个档案比第二个档案新

    -ot	第一个档案比第二个档案旧

    -ef	第一个档案与第二个档案为同一个档案（ link 之类的档案）

5. 逻辑的『和(and)』『或(or)』

    &&	逻辑的 AND 的意思

    ||	逻辑的 OR 的意思

###### 运算符号 

=	等于 应用于：整型或字符串比较 如果在[] 中，只能是字符串

!=	不等于 应用于：整型或字符串比较 如果在[] 中，只能是字符串

<	小于 应用于：整型比较 在[] 中，不能使用 表示字符串

\>	大于 应用于：整型比较 在[] 中，不能使用 表示字符串

-eq	等于 应用于：整型比较

-ne	不等于 应用于：整型比较

-lt	小于 应用于：整型比较

-gt	大于 应用于：整型比较

-le	小于或等于 应用于：整型比较

-ge	大于或等于 应用于：整型比较

-a	双方都成立（and） 逻辑表达式 –a 逻辑表达式

-o	单方成立（or） 逻辑表达式 –o 逻辑表达式

-z	空字符串

-n	非空字符串

