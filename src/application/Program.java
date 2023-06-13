package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department depart = new Department(1, "Livros");
		
		Seller sell = new Seller(22,"Weslley" ,"weslley@terra.com.br", new Date(), 3000.0, depart);
		
		System.out.println(sell);
		

	}

}
