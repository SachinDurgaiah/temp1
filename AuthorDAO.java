package com.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms.entity.Author;
import com.lms.entity.Book;

@SuppressWarnings("unchecked")
public class AuthorDAO extends BaseDAO{

	public AuthorDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	public void addAuthorWithBook(int au_id,int b_id) throws ClassNotFoundException, SQLException{
		save("insert into tbl_book_authors (bookId,authorId) values (?,?)", new Object[] {b_id,au_id});
	}
	public void addAuthor(Author author) throws ClassNotFoundException, SQLException{
		save("insert into tbl_author (authorName) values (?)", new Object[] {author.getAuthorName()});
	}
	public Integer addAuthorWithID(Author a) throws ClassNotFoundException, SQLException{
		return saveWithID("insert into tbl_author (authorName) values (?)", new Object[] {a.getAuthorName()});
	}
	
	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException{
		save("update tbl_author set authorName = ? where authorId = ?", new Object[] {author.getAuthorName(), author.getAuthorId()});
	}
	
	public void deleteAuthor(Integer authorId) throws ClassNotFoundException, SQLException{
		save("delete from tbl_book_authors where authorId = ?",new Object[]{authorId});
		save("delete from tbl_author where authorId = ?",new Object[]{authorId});
		
	}
	
	public List<Author> readAllAuthors() throws ClassNotFoundException, SQLException{
		return (List<Author>) readAll("select * from tbl_author", null);
	}
	
	public List<Author> readAuthorsByName(String name) throws ClassNotFoundException, SQLException{
		return (List<Author>) readAll("select * from tbl_author where authorName like ?", new Object[] {name});
	}
	
	public Integer getCount() throws ClassNotFoundException, SQLException{
		return getCount("select count(*) from tbl_author",null);
	}
	
	
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<Author>();
		BookDAO bdao = new BookDAO(getConnection());
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			try {
				a.setBooks((List<Book>) bdao.readFirstLevel("select * from tbl_book where bookId IN (select bookId from tbl_book_authors where authorId = ?)", new Object[] {a.getAuthorId()}));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			authors.add(a);
		}
		return authors;
	}
	
	@Override
	public List<Author> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<Author>();
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			
			authors.add(a);
		}
		return authors;
	}

	public Author readAuthorsByID(Integer authorId) throws ClassNotFoundException, SQLException {
		List<Author> authors = (List<Author>) readAll("select * from tbl_author where authorId = ?", new Object[] {authorId});
		if(authors!=null && authors.size() >0){
			return authors.get(0);
		}
		return null;
	}

	public void changeAuthor(String au_Id, String authorName) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		int a_id=Integer.parseInt(au_Id);
		//int au_name=Integer.parseInt(authorName);
		System.out.println("Final steep of updating author");
		save("update tbl_author set authorName=? where authorId=?", new Object[] {authorName,au_Id});
		
	}

}
