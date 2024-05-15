package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Scanner;

public class Books {
    static Scanner sc = new Scanner(System.in);

    //show all books entery
    public static void ShowBooks() {

        try{
            Connection conn = DbConnect.getConnection();
            String sql = "SELECT *  FROM books";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                System.out.println("BOOK ID :" + rs.getInt(1));
                System.out.println("BOOK NAME : "+rs.getString(2));
                System.out.println("BOOK AUTHOR :"+rs.getString(3));
                System.out.println("SBIN :"+rs.getString(4));
                System.out.println("QUANTITY AVAILABLE :"+rs.getInt(5));
                System.out.println("-------------------------------------------------------");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void OrderBook(int id){
        System.out.println("Enter Book id :");
        int uid = sc.nextInt();
        try{
            Connection conn = DbConnect.getConnection();
            String sql ="SELECT * FROM books WHERE bid=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1,uid);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                int qty = rs. getInt(5);
                if(qty>0){
                    String query = "INSERT INTO orders(uid,bid)VALUE(?,?)";
                    PreparedStatement ps = conn.prepareStatement(query);

                    ps.setInt(1,id);
                    ps.setInt(2,uid);
                    int ar = ps.executeUpdate();
                    if(ar>0){
                        String s = "UPDATE books SET qty=? WHERE bid=?";
                        PreparedStatement p = conn.prepareStatement(s);
                        p.setInt(1,qty-1);
                        p.setInt(2,uid);
                        int arows = p.executeUpdate();
                        if(arows>0)
                            System.out.println("Book Order Successfully");
                            else
                                System.out.println("Failed !"); 

                        }else
                        System.out.println("Failed to order");
                    }else{
                        System.out.println("Book Quantity is Zero \n Try After Some Time !\n");

                    }

                }else{
                    System.out.println("Book is not Available");

                }
            }catch(Exception e ){
                e.printStackTrace();
            }

        }
        // view all order
        public static void ViewOrder(){
            try{
                String sql = "SELECT user.udi,user.fname,user.lname,user.gender,books.bookname FROM user JOIN orders ON user.uid=orders.uid JOIN books ON orders.bid=books.bid";
                PreparedStatement pst = DbConnect.getConnection().prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                System.out.println("ID\tFname\tLname\tgender\tBookName");
                while(rs.next()){
                    System.out.println(rs.getInt(1)+"\t");
                    System.out.println(rs.getString(2)+"\t");
                    System.out.println(rs.getString(3)+"\t");
                    System.out.println(rs.getString(4)+"\t");
                    System.out.println(rs.getString(5)+"\n");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //View all order by id
        public static void ViewOrderById(Scanner sc){
            System.out.println("Enter Student id : ");
            int id = sc.nextInt();
            try{
                String sql = "SELECT user.uid,user.fname,user.gender,orders.id,orders.sts,books.bookname FROM user JOIN orders ON user.uid=orders.uid JOIN books ON orders.bid=books.bid WHERE user.uid=?";
                PreparedStatement pst = DbConnect.getConnection().prepareStatement(sql);
                pst.setInt(1,id);
                ResultSet rs = pst.executeQuery();
                System.out.println("ID\tFname\tGender\tOrderID\tStatus\tBookName");
                    while(rs.next()){
                        System.out.println(rs.getInt(1)+"\t");
                        System.out.println(rs.getString(2)+"\t");
                        System.out.println(rs.getString(3)+"\t");
                        System.out.println(rs.getString(4)+"\t");
                        String status;
                        if(rs.getInt(5)==1)
                        status="Issues";
                        else
                            status="Retured";
                            System.out.println(status+"\t");
                            System.out.println(rs.getString(6)+"\n");

                    }
                    System.out.println("\nEnter order ID to Return Books : ");
                    int oid = sc.nextInt();
                    String s = "UPDATE order SET sts=0 WHERE id=? AND sts=1";

                    PreparedStatement ps = DbConnect.getConnection().prepareStatement(s);
                    ps.setInt(1,oid);
                    int ar = ps.executeUpdate();
                    if(ar>0){
                        System.out.println("Book Return Successfully !\n");
                        String qr ="SELECT * FROM orders WHERE id=?";
                        PreparedStatement p = DbConnect.getConnection().prepareStatement(qr);

                        p.setInt(1,oid);
                        ResultSet r = p.executeQuery();
                        int bid=0;
                        if(r.next()){
                            bid=r.getInt(3);

                        
                        }

                        String st = "SELECT * FROM books WHERE bid=?";
                        PreparedStatement pp = DbConnect.getConnection().prepareStatement(st);
                        pp.setInt(1,bid);
                        ResultSet rr = pp.executeQuery();
                        int qty=0;
                        if(rr.next()){
                            qty=rr.getInt(5);
                        }
                        String ss = "UPDATE books SET quy=? WHERE bid=?";
                        PreparedStatement pts = DbConnect.getConnection().prepareStatement(ss);
                        pts.setInt(1,qty+1);
                        pts.setInt(2,bid);

                        int arows = pts.executeUpdate();
                        if(arows>0)
                            System.out.println("And Update Successfully !");

                    }else
                            System.out.println("Failed to return! \n");
                        
                        }catch(Exception e){
                            e.printStackTrace();

                        }

                    }
                    public static void AddBook(Scanner sc){
                        System.out.println("Enter book Name : ");
                        String name = sc.next();
                        System.out.println("Enter Author Name : ");
                        String aname = sc.next();
                        System.out.println("Enter SBIN Number : ");
                        String sbin = sc.next();
                        System.out.println("Enter Quantity : ");
                        int qty = sc.nextInt();

                        try{
                            String sql = "INSERT INTO books(bookname, bookauthor, sbin, qty) VALUE(?,?,?,?);";
                            PreparedStatement pst = DbConnect.getConnection().prepareStatement(sql);
                            pst.setString(1,name);
                            pst.setString(2,aname);
                            pst.setString(3,sbin);
                            pst.setInt(4,qty);
                            int ar = pst.executeUpdate();
                            if(ar>0)
                                System.out.println("Book Added Successfully !");
                            else
                            System.out.println("Failed to ADD!");
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                    public static void DeleteBook(Scanner sc){
                        System.out.println("Enter book Id : ");
                        int bid = sc.nextInt();
                        try{
                            String str = "DELETE FROM books WHERE bid=?";
                            PreparedStatement ps = DbConnect.getConnection().prepareStatement(str);
                            ps.setInt(1,bid);

                            int ar = ps.executeUpdate();
                            if(ar>0)
                            System.out.println("Book Deleted Succwssfully !");
                            else
                            System.out.println("Failed to Delete!");

                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }

            }
        
        

    


