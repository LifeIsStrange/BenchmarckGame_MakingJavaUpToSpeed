# BenchmarckGame_MakingJavaUpToSpeed
OKLM

you'll need openjdk >= 16 e.g through sdkman you can do sdk use java 16.0.0.hs-adpt (share the same lifetime as the shell like e.g a pyEnv)
Currenlty (2021.1 beta 3) intellij doesn't allow to sucessfully run a project using the Vector API so run shadowjar and then run on your shell:
java --enable-preview --add-modules=jdk.incubator.vecto -jar spectralNorm-1.0-SNAPSHOT-all.jar

the current reference output is 2.113009097

remaining TODOs:

* replace adds by horizontal adds
* bench: I get on average ~0.08 seconds on my machine while an equivalent C# version run in ~0.03 seconds for n = 100 and respectively ~1.68 and ~9.4 seconds for n = 10K
Conclusions: well no conclusion can be drawn before horizontal adds are added, however preliminary results seems to indicate that the JDK has a higher "fixed" cost[0] for small inputs but has significantly better througput for larger inputs.
* optimize

[0] well maybe that some of it is due to some workarounds/inneficiencies I had to do because of not always finding a one to one API correspondance with the C# implementation.

Notes:
The benchmark is somehow unfair given that the C# implementation uses:
* A Span aka stack allocated array
* manual memory management (unsafe fixed pointers)
* explictit inlining and compiler hints (hot)
* the implementation has been written for C# first and then ported so no trial and error of code shapes for java
* 
Despite this, openjdk seems to be multiple times faster on "large" input sizes.

Question: is .NET (5.0.4) faster on Windows ?
