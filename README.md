# BenchmarckGame_MakingJavaUpToSpeed
OKLM

you'll need openjdk >= 16 e.g through sdkman you can do sdk use java 16.0.0.hs-adpt (share the same lifetime as the shell like e.g a pyEnv)
Currenlty (2021.1 beta 3) intellij doesn't allow to sucessfully run a project using the Vector API so run shadowjar and then run on your shell:
java --enable-preview --add-modules=jdk.incubator.vecto -jar spectralNorm-1.0-SNAPSHOT-all.jar

the current reference output is 1.274224153

NEW RESULTS WITH HORIZONTAL ADD emulation:
By moving an array allocation out of a hot loop I was able to get 
1.09 second for the Java implementation at n = 5500 and 1.01 second for C# at same n. Some tuning will be needed.

[0] well maybe that some of it is due to some workarounds/inneficiencies I had to do because of not always finding a one to one API correspondance with the C# implementation.

Notes:
The benchmark is somehow unfair given that the C# implementation uses:
* A Span aka stack allocated array
* manual memory management (unsafe fixed pointers)
* explictit inlining and compiler hints (hot)
* the implementation has been written for C# first and then ported so no trial and error of code shapes for java

Question: is .NET (5.0.4) faster on Windows ?
