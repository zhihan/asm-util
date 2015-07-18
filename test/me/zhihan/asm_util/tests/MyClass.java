package me.zhihan.asm_util.tests;


public class MyClass extends Abs {
	@MyAnnotation(name = "Jordan", numbers = {1, 2})
	public void test() {
		System.out.println("Hello");
	}

	@Override 
	public String name() {
		return "sub";
	}

	public static void main(String[] args) {
		MyClass obj = new MyClass();
		System.out.println(obj.name());
	}

}