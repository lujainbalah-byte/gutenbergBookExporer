package com.student;

import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

    static Scanner input = new Scanner(System.in);

    static class Book {
        String title;
        String author;
        String url;
        String price;

        Book(String title, String author, String url, String price) {
            this.title = title;
            this.author = author;
            this.url = url;
            this.price = price;
        }
    }

    static Book[] books = new Book[100];
    static Book[] readingList = new Book[20];
    static int bookCount = 0;
    static int readingCount = 0;

    public static void main(String[] args) {

        loadBooks();

        int choice;

        do {
            System.out.println("\n===== GUTENBERG BOOK EXPLORER =====");
            System.out.println("1. Explore Books");
            System.out.println("2. Search by Title");
            System.out.println("3. Search by Author");
            System.out.println("4. Add Book to Reading List");
            System.out.println("5. View Reading List");
            System.out.println("6. Remove Book");
            System.out.println("7. Display Book Details");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.nextLine();
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
    exploreBooks();
    break;
                case 2:
                    System.out.print("Enter title: ");
                    searchBook(input.nextLine());
                    break;
                case 3:
                    System.out.print("Enter author: ");
                    searchBook(input.nextLine(), true);
                    break;
                case 4:
                    exploreBooks();
                    addBook();
                    break;
                case 5:
                    viewReadingList();
                    break;
                case 6:
                    removeBook();
                    break;
                case 7:
                    displayBookDetails();
                    break;
                case 0:
                    System.out.println("Thank you for using the program!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    static void loadBooks() {
        try {
            for (int page = 1; page <= 5; page++) {
                String url = "https://books.toscrape.com/catalogue/page-" + page + ".html";

                Document doc = Jsoup.connect(url).get();
                Elements items = doc.select("article.product_pod");

                for (Element item : items) {
                    Element link = item.selectFirst("h3 a");
                    Element price = item.selectFirst(".price_color");

                    String title = link.attr("title");
                    String bookUrl = link.absUrl("href");
                    String bookPrice = price.text();

                    books[bookCount] = new Book(
                        title,
                        "Not provided",
                        bookUrl,
                        bookPrice
                    );

                    bookCount++;
                }
            }

            System.out.println(bookCount + " books loaded successfully.");

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    static void exploreBooks() {
    System.out.println("\nAvailable Books:");

    if (bookCount == 0) {
        System.out.println("No books available.");
        return;
    }

    for (int i = 0; i < Math.min(10, bookCount); i++) {
        System.out.println("\nBook Number: " + (i + 1));
        System.out.println("Title: " + books[i].title);
        System.out.println("Price: " + books[i].price);
        System.out.println("URL: " + books[i].url);
    }
}

    static void searchBook(String title) {
        boolean found = false;

        for (int i = 0; i < bookCount; i++) {
            if (books[i].title.toLowerCase().contains(title.toLowerCase())) {
                System.out.println(books[i].title);
                System.out.println(books[i].url);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found.");
        }
    }

    static void searchBook(String keyword, boolean byAuthor) {
        if (byAuthor) {
            System.out.println("Author information is not provided by this website.");
        } else {
            searchBook(keyword);
        }
    }

    static void addBook() {
        if (readingCount >= readingList.length) {
            System.out.println("Reading list is full.");
            return;
        }

        System.out.print("Enter book number: ");
        int number = input.nextInt();
        input.nextLine();

        if (number < 1 || number > bookCount) {
            System.out.println("Invalid book number.");
            return;
        }

        readingList[readingCount] = books[number - 1];
        readingCount++;

        System.out.println("Book added successfully.");
    }

    static void viewReadingList() {
        if (readingCount == 0) {
            System.out.println("Reading list is empty.");
            return;
        }

        for (int i = 0; i < readingCount; i++) {
            System.out.println((i + 1) + ". " + readingList[i].title);
        }
    }

    static void removeBook() {
        viewReadingList();

        if (readingCount == 0) return;

        System.out.print("Enter book number to remove: ");
        int number = input.nextInt();
        input.nextLine();

        if (number < 1 || number > readingCount) {
            System.out.println("Invalid number.");
            return;
        }

        for (int i = number - 1; i < readingCount - 1; i++) {
            readingList[i] = readingList[i + 1];
        }

        readingList[readingCount - 1] = null;
        readingCount--;

        System.out.println("Book removed successfully.");
    }

    static void displayBookDetails() {
        exploreBooks();

        System.out.print("Enter book number: ");
        int number = input.nextInt();
        input.nextLine();

        if (number < 1 || number > bookCount) {
            System.out.println("Invalid number.");
            return;
        }

        Book b = books[number - 1];

        System.out.println("Title: " + b.title);
        System.out.println("Author: " + b.author);
        System.out.println("Price: " + b.price);
        System.out.println("URL: " + b.url);
    }
}