package no.tillung.test;

import java.util.Scanner;

public class InputTest {

	public static void main(String[] args) {
		System.out.print("command: ");
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		System.out.println("Lest: " + input);
	}

}
