# Half-assed tiny subset of Go language to C translator

* College assignment - 2013
* Written in Java
* There is no garbage collector

# Transpile (go -> c):
```
make
java Main > example.c
```

## Run transpiled (c)
```
gcc example.c
./a.out
```

## Run original (go)
```
go run example.go
```
