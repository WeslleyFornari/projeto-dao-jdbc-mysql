package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

//MAIN - TELA - VIEW
public class Program {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		SellerDao sellerDao = DaoFactory.createSellerDao(); // CRIA CONEXÃO COM O BANCO DE DADOS

		// ---------------------------------------------------------------------------------------------------------
		System.out.println(" ==== TESTE 1 = SELLER findById ===="); // findById
		Seller sell = sellerDao.findById(3);
		System.out.println(sell);
		// ---------------------------------------------------------------------------------------------------------

		System.out.println(" \n ==== TESTE 2 = SELLER findByDepartement ===="); // findByDepartment
		Department dep = new Department(2, null);
		List<Seller> lista = sellerDao.findByDepartment(dep);
		for (Seller obj : lista) {
			System.out.println(obj);
		}
		// ---------------------------------------------------------------------------------------------------------

		System.out.println(" \n ==== TESTE 3 = SELLER findAll ===="); // findAll
		lista = sellerDao.findAll();
		for (Seller obj : lista) {
			System.out.println(obj);

		}
		// ---------------------------------------------------------------------------------------------------------

//		System.out.println(" \n ==== TESTE 4 = SELLER Insert ===="); // Insert
//		Seller novoSeller = new Seller(null, "Kika", "kika@terra.com.br", new Date(), 2987.00, dep);
//		sellerDao.insert(novoSeller);
//		System.out.println("Inserido new ID: " + novoSeller.getId());

		// ---------------------------------------------------------------------------------------------------------

//		System.out.println(" \n ==== TESTE 5 = SELLER Update ===="); // UpDate
//		sell = sellerDao.findById(9);
//		sell.setName("Hieldys Fornari");
//		//sell.setBaseSalary(3500.00);
//		sellerDao.update(sell);
//		System.out.println("Update completo!");
		
// ---------------------------------------------------------------------------------------------------------

		System.out.println(" \n ==== TESTE 5 = SELLER Delete ===="); // Delete
        System.out.println("Entre com Id Usuario para deletar: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Id: "+ id + " deletado");
        
        sc.close();
        
	}
}
