package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


// INJECAO DE DEPENDENCIA DAO
public class SellerDaoJDBC implements SellerDao {

	private Connection conn; // OBJ DE CONEXÃO
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null; 
		ResultSet rs = null; 
		
		// COMANDO SQL
	try {
			st = conn.prepareStatement(             
					"SELECT seller.*,department.Name as DepName "                            
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);                                       
			rs = st.executeQuery();                      
			
			// SE -> EXISTIR A LINHA CHAMADA
			if (rs.next()) {
				// CHAMA OS METODOS
				Department dep = instantiateDepartment(rs);
				Seller sell = instantiateSeller(rs, dep);
				return sell;
			 }
			 return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);                         
			DB.closeResultSet(rs);                            
		}
	}
//----------------------------------------------------------------------------------------------------------------------------	

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(" INSERT INTO seller "
		                              +"(Name, Email, BirthDate, BaseSalary, DepartmentId)"
		                              +" VALUES "
		                              +" (?, ?, ?, ?, ?)",
		                              Statement.RETURN_GENERATED_KEYS); // retorna o ID inserido
                      
			          st.setString(1, obj.getName());
			          st.setString(2, obj.getEmail());
			          st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			          st.setDouble(4, obj.getBaseSalary());
			          st.setInt(5, obj.getDepartment().getId());
			          
			          int rowsAffected = st.executeUpdate(); // executa Update
			          
			          if (rowsAffected > 0 ) {
			        	  ResultSet rs = st.getGeneratedKeys();
			        	  if (rs.next()) {
			        		  int id = rs.getInt(1);
			        		  obj.setId(id);
			        	  }
			        	DB.closeResultSet(rs);  
			          }
			          else {
			        	  throw new DbException("Erro, nenhuma linha foi alterada.");
			          }
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	} 

// ----------------------------------------------------------------------------------------------------------------------
	@Override
	public void update(Seller obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(" UPDATE seller"
				                      + " SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " 
							          + " WHERE Id = ? ");
                      
			          st.setString(1, obj.getName()); // PLACEHOLDERS
			          st.setString(2, obj.getEmail());
			          st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			          st.setDouble(4, obj.getBaseSalary());
			          st.setInt(5, obj.getDepartment().getId());
			          st.setInt(6, obj.getId());
			          
			          st.executeUpdate();
			     
		    }
		    catch (SQLException e) {
			     throw new DbException(e.getMessage());
		    }
	     	finally {
			     DB.closeStatement(st);
		    }
		
	}
// ----------------------------------------------------------------------------------------------------------------------
	
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(" DELETE FROM seller WHERE Id = ? "); // Comando SQL
			
			st.setInt(1, id); //Id selecionado para exclusão
			st.executeUpdate(); // executa a ação
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
        finally {
        	DB.closeStatement(st);
        }
		
	}

// -------------------------------------------------------------------------------------------------------------------
	@Override
	public List<Seller> findAll() { 
		
		PreparedStatement st = null; 
		ResultSet rs = null;  
		
		// COMANDO SQL
	try {
			st = conn.prepareStatement(             
					"SELECT seller.*,department.Name as DepName "             
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					// + "WHERE Department.Id = ?"); ----esta linha não precisa 
					+ "ORDER BY Name");
			
			rs = st.executeQuery();                                                  
			
			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			// ENQUANTO -> EXISTIR O PROXIMO
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId")); /* MAP se existe depId, senão retorna null */
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sell = instantiateSeller(rs, dep);
				lista.add(sell);
				
			 }
			 return lista;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st); 
			DB.closeResultSet(rs); 
		}
	}

// -------------------------------------------------------------------------------------------------------------------
	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement st = null; 
		ResultSet rs = null;  
		
		// COMANDO SQL
	try {
			st = conn.prepareStatement(             
					"SELECT seller.*,department.Name as DepName "             
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE Department.Id = ?");
					//+ "ORDER BY Name");
			
			st.setInt(1, department.getId());                                    
			rs = st.executeQuery();                                                  
			
			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			// ENQUANTO -> EXISTIR O PROXIMO
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId")); /* MAP se existe depId, senão retorna null */
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sell = instantiateSeller(rs, dep);
				lista.add(sell);
				
			 }
			 return lista;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st); 
			DB.closeResultSet(rs); 
		}
	}
//------------------------------------------------------------------------------------------------------------------------------	
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
    	   
    	    Seller sell = new Seller();        
	    
		  // OBJETO   <--   TABELA RS    	    
		    sell.setId(rs.getInt("Id"));
		    sell.setName(rs.getString("Name"));
		    sell.setEmail(rs.getString("Email"));
		    sell.setBaseSalary(rs.getDouble("Basesalary"));
		    sell.setBirthDate(rs.getDate("BirthDate"));
		    sell.setDepartment(dep);
		    return sell;
    }	    
		   
	
//------------------------------------------------------------------------------------------------
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		
		 Department dep = new Department(); 
		
     // OBJETO   <--    TABELA RS			 
		 dep.setId(rs.getInt("DepartmentId"));
		 dep.setName(rs.getString("DepName"));
	     return dep;
    }
}
	