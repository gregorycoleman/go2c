package main

import "fmt"

// returns true if character 'c' is lowercase
func is_lower(c int) bool {
        return c >= 'a' && c <= 'z'
}

// returns character 'c' converted to uppercase
func to_upper(c int) int {
        if is_lower(c) {
                return c - 'a' + 'A'
        }
        return c
}

// returns the string 'a' converted to uppercase
func uppercase(a string) string {
	s := ""
        for i := 0; i < len(a); i = i+1 {
                s = s + string(to_upper(int(a[i])))
        }
        return s
}

// returns the string val inverted
func invertString(val string) string {
	ret := ""

	for i := len(val) - 1; i >= 0; i = i - 1 {
		ret = ret + string(val[i])
	}

	return ret
}

// converts 'val' to its string representation
func intToString(val int) string {
	ret := ""
	neg := val < 0

	if val == 0 {
		return "0"
	}

	if neg {
		val = -val
	}

	for i := val; i > 0; i = i/10 {
		c := i % 10
		ret = ret + string(c + '0')
	}

	ret = invertString(ret)

	if neg {
		ret = "-" + ret
	}

	return ret
}

func factorial(n int) int {
	if n == 1 {
		return 1
	}
	return factorial(n - 1) * n
}

// program start here
func main() {
	fmt.Println(invertString("Hello World"))
	fmt.Println(uppercase("uppercase"))
	fmt.Println(intToString(-234))
	fmt.Println(intToString(60))
	fmt.Println(intToString(factorial(6)))
}
