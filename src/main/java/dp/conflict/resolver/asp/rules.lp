% GENERATION OF ADDITIONAL FACTS

% compute all includeJar facts based on all jar facts, dont include 0 because this already indicates the root
{includeJar(X)} :- jar(X,_,_,_), X != 0.

% a method that must be included is one that is called by the root project (ID: 0), from a invocation
mustIncludeMethod(C,N,S) :- invocation(0,_,C,N,S).

% facts for each method that comes with the included jar
includeMethod(C,N,S) :- includeJar(X), method(X,C,N,S), jar(X,_,_,_).

% compute missing methods that are not covered by the included jars but must be included
missingMethod(C,N,S) :- method(_,C,N,S), mustIncludeMethod(C,N,S), not includeMethod(C,N,S).

% generate clash facts based on the usage of methods with same names and different signatures
clash(X,X2,C,N) :- includeJar(X), includeJar(X2), jar(X,Y,Z,V), jar(X2,Y,Z,V2), method(X,C,N,S), method(X2,C,N,S2), X != X2, S != S2.
% generate clash facts if two jars are included whose connections (dependencies) each conatin methods with potential clash
clash(D,D2,C,N) :- includeJar(X), includeJar(X2), connection(X,D), connection(X2,D2), jar(D,Y,Z,V), jar(D2,Y,Z,V2), method(D,C,N,S), method(D2,C,N,S2), D != D2, S != S2.

% CONSTRAINTS
% if a minimum amount of included jars are satisfying the problem, do not add other jars
% TODO

% the amount of clashes must be 0
:- #count{X,X2,C,N : clash(X,X2,C,N)} != 0.

% the amount of missing methods must be 0
:- #count{C,N,S : missingMethod(C,N,S)} != 0.

#show includeJar/1.
%#show mustIncludeMethod/3.
#show missingMethod/3.
#show clash/4.

