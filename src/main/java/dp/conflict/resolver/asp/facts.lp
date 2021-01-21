
jar(1,"org.runtime.conflict","Project_D","1.0").
class(1,"conflict.Object_D").
method(1,"conflict.Object_D","conflict.Object_D",0).
method(1,"conflict.Object_D","bar",2).
method(1,"conflict.Object_D","kek",1).


jar(2,"org.runtime.conflict","Project_D","2.0").
class(2,"conflict.Object_D").
method(2,"conflict.Object_D","conflict.Object_D",0).
method(2,"conflict.Object_D","bar",1).
method(2,"conflict.Object_D","kek",1).


jar(3,"org.runtime.conflict","Project_D","3.0").
class(3,"conflict.Object_D").
method(3,"conflict.Object_D","conflict.Object_D",0).
method(3,"conflict.Object_D","bar",1).
method(3,"conflict.Object_D","kek",1).
class(3,"conflict.ExtraObject_D").
method(3,"conflict.ExtraObject_D","conflict.ExtraObject_D",0).
method(3,"conflict.ExtraObject_D","bar",1).
method(3,"conflict.ExtraObject_D","bar",2).

jar(4,"org.runtime.conflict","Project_B","1.0").
class(4,"runtime.Object_B").
method(4,"runtime.Object_B","runtime.Object_B",0).
method(4,"runtime.Object_B","foo",1).
method(4,"runtime.Object_B","test",0).
connection(4,1).


jar(5,"org.runtime.conflict","Project_B","2.0").
class(5,"runtime.Object_B").
method(5,"runtime.Object_B","runtime.Object_B",0).
method(5,"runtime.Object_B","foo",1).
method(5,"runtime.Object_B","test",0).
connection(5,3).

invocation(0,4,"runtime.Object_B","foo",1).

invocation(4,1,"conflict.Object_D","bar",2).

invocation(4,1,"conflict.Object_D","kek",1).

invocation(4,1,"conflict.Object_D","kek",1).
connection(4,1).

jar(6,"org.runtime.conflict","Project_C","1.0").
class(6,"runtime.Object_C").
class(6,"runtime.CInterface").
method(6,"runtime.Object_C","runtime.Object_C",0).
method(6,"runtime.Object_C","foo",1).
connection(6,2).


jar(7,"org.runtime.conflict","Project_C","2.0").
class(7,"runtime.Object_C").
method(7,"runtime.Object_C","runtime.Object_C",0).
method(7,"runtime.Object_C","foo",1).
class(7,"runtime.CInterface").
method(7,"runtime.CInterface","foo",1).
connection(7,3).

invocation(0,4,"runtime.Object_B","foo",1).

invocation(0,6,"runtime.Object_C","foo",1).

invocation(0,4,"runtime.Object_B","foo",1).

invocation(6,2,"conflict.Object_D","bar",1).
connection(6,2).
