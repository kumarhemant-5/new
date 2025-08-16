package com.makesoft.course;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class Library {
    String bookName;
    String author;

    Library(){

    }

    @Override
    public int hashCode(){
        int hash =3;
        hash =83* hash+ Objects.hashCode(this.bookName);
        hash =83* hash+ Objects.hashCode(this.author);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj)return true;
        if(obj == null)return false;
        if(getClass() != obj.getClass())return false;

        final Library other = (Library) obj;

        if(!Objects.equals(this.bookName, other.bookName))return false;
        if(!Objects.equals(this.author,other.author)) return false;

        return true;
    }

    Library(String bookName, String author){
        this.bookName=bookName;
        this.author=author;
    }

    public HashMap<Integer,Library> createLibraryMap(String booksInLibrary){
        HashMap<Integer,Library> map = new HashMap<>();
        String [] booksDetail= booksInLibrary.split("\\|");
        for(String bookDetail: booksDetail){
            String [] book = bookDetail.split(",");
            Integer bookId = Integer.parseInt(book[0]);
            String bookName = book[1];
            String bookAuthor= book[2];

            Library library = new Library(bookName,bookAuthor);
            map.put(bookId,library);
        }
        return map;
    }

    public HashMap<Integer,Integer> createUserMap(String borrowedUsers){
        HashMap<Integer,Integer> map = new HashMap<>();
        String [] booksDetail= borrowedUsers.split("\\|");
        for(String bookDetail: booksDetail){
            String [] book = bookDetail.split(",");
            Integer bookId = Integer.parseInt(book[0]);
            Integer userId = Integer.parseInt(book[1]);
            map.put(bookId,userId);
        }
        return map;
    }

        public String getQuery(String booksInLibrary, String borrowedUsers, String query){
        HashMap<Integer,Library> library = createLibraryMap(booksInLibrary);
        HashMap<Integer,Integer> borrow = createUserMap(borrowedUsers);

        String [] queryDetail = query.split(",");
        String com = queryDetail[0];
        String ans="";
        if(com.equals("1")){
            boolean result = borrow.containsKey(Integer.parseInt(queryDetail[1]));
            if(result){
                ans="No Stock\nIt is owned by "+borrow.get(Integer.parseInt(queryDetail[1]))+"\n";
            }else{
                ans="It is available\nAuthor is "+library.get(Integer.parseInt(queryDetail[1])).author+"\n";
            }
        }else if(com.equals("2")){
            if(borrow.containsValue(Integer.parseInt(queryDetail[1]))){
                List<Integer> list =borrow.entrySet().stream().filter(e->e.getValue()==Integer.parseInt(queryDetail[1]))
                        .map(e->e.getKey()).collect(Collectors.toList());
                for(Integer e: list){
                    ans +=(e+" "+library.get(e).bookName+"\n");
                }
            }else{
                ans= "No book borrowed by: "+Integer.parseInt(queryDetail[1]);
            }
        }else if(com.equals("3")){
            List<Integer> list =library.entrySet().stream().filter(e->e.getValue().bookName.equals("Data Structure"))
                    .map(e->e.getKey()).collect(Collectors.toList());
            int count=0;
            for(Integer e: list){
                if(borrow.containsKey(e))count++;
            }
            ans= count+" out\n"+(list.size()-count)+" in\n";
        }else if(com.equals("4")){
            List<String> list= library.entrySet().stream().filter(e->e.getValue().author.equals(queryDetail[1])).map(e->e.getValue().bookName)
                    .collect(Collectors.toList());
            for (String e: list){
                ans+=e+"\n";
            }
        }else{
            List<Integer> list =library.entrySet().stream().filter(e->e.getValue().bookName.toLowerCase().contains(queryDetail[1].toLowerCase())).map(e->e.getKey())
                    .collect(Collectors.toList());
            for(Integer e: list){
                ans+=e+" "+library.get(e).bookName+"\n";
            }
        }
        return ans;
    }
}
