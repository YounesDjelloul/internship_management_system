import java.io.*;
import java.util.*;

class Menu {

  String loggedUser;

  String[] menu = {
    "1. Create New Position",
    "2. Get All Positions",
    "3. Delete Position",
    "4. Apply for Position",
    "5. Login to system",
    "6. Signup to system",
    "7. Create a profile",
    "8. Get Profile",
    "0. To Exit",
  };

  public void showMenu() {

    System.out.println("\n----------------------- Welcome to the system: --------------------------\n");

    Position positionObj = new Position();
    UserClass userObj    = new UserClass();
    Profile profileObj   = new Profile();

    Scanner menuScan = new Scanner(System.in);

    for(String option: this.menu) {
      System.out.println(option);
    }

    System.out.print("\nChoose an option from the above menu: ");
    int selectedOption = menuScan.nextInt();

    switch(selectedOption) {
      case 1:
        positionObj.create();
        this.showMenu();
        break;
      case 2:
        positionObj.list();
        this.showMenu();
        break;
      case 3:
        positionObj.delete();
        this.showMenu();
        break;
      case 4:
        positionObj.apply(this.loggedUser);
        this.showMenu();
        break;
      case 5:
        this.loggedUser = userObj.login();
        this.showMenu();
        break;
      case 6:
        userObj.signUp();
        this.showMenu();
        break;
      case 7:
        profileObj.createStudentProfile(this.loggedUser);
        this.showMenu();
      case 8:
        profileObj.getProfile(this.loggedUser);
        this.showMenu();
      case 0:
        break;
      default:
        System.out.println("\nInvalid Option!");
        this.showMenu();
    }
  }
}

class Position {

  String[] form = {
    "Company Name",
    "Position name",
    "Position budget",
    "Position requirments",
  };

  public void create() {
    try {

      System.out.println("\nWelcome to the position form! \n");

      File file = new File("database.txt");
      file.createNewFile();

      Scanner fileScan = new Scanner(file);
      String lastID = "init";

      while(fileScan.hasNextLine()) {
        lastID = fileScan.nextLine().split(",")[0];
      }

      FileWriter myWriter = new FileWriter(file.getName(), true);

      if(lastID == "init") {
        myWriter.write("\n"+1+",");
      } else {
        myWriter.write("\n"+Integer.sum(Integer.parseInt(lastID), 1)+",");
      }

      Scanner inputScan   = new Scanner(System.in);
      System.out.println("\n");

      for(String input: this.form) {
        System.out.print(input + ": ");
        String currentValue = inputScan.nextLine();

        myWriter.write(input + ": " + currentValue+",");
      }

      System.out.println("\nPosition Created Successfully!");
      
      myWriter.close();
      fileScan.close();

    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void list() {

    try {
      System.out.println("\nAll available positions!");

      File file = new File("database.txt");
      Scanner fileScan = new Scanner(file);

      while(fileScan.hasNextLine()) {

        String line   = fileScan.nextLine();
        String[] data = line.split(",");

        System.out.println("\nPosition number " + data[0] + "\n");

        data = Arrays.copyOfRange(data, 1, data.length);

        for(String one: data) {
          System.out.println(one);
        }
      }

      System.out.print("\n");

      fileScan.close();

    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void delete() {
    File file        = new File("database.txt");
    File tempFile    = new File("tempDatabase.txt");

    try {
      System.out.println("\nDelete a position!\n");

      Scanner inputScan = new Scanner(System.in);
      System.out.print("The ID of position you want to delete: ");
      String idToDelete = inputScan.nextLine();

      Scanner fileScan = new Scanner(file);

      FileWriter myWriter = new FileWriter(tempFile.getName(), true);

      boolean deleted = false;

      while(fileScan.hasNextLine()) {

        String line              = fileScan.nextLine();
        String currentPositionId = line.split(",")[0];

        System.out.println(currentPositionId);
        System.out.println(idToDelete);

        if (currentPositionId.equals(idToDelete)) {
          deleted = true;
          continue;
        }

        myWriter.write(line);
      }

      if(deleted == false) {
        System.out.println("\nPostion couldn't be found");
      } else {
        System.out.println("\nPosition Deleted Successfully!\n");
      }

      myWriter.close();
      fileScan.close();

      file.delete();
      tempFile.renameTo(file);

    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void apply(String loggedUser) {

    Profile profileObj = new Profile();

    if (loggedUser == null || loggedUser == "") {
      System.out.println("\nLogin to apply for a position");
    } else {
      try {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Provide the position number: ");
        String positionNumber = scanner.nextLine();
      
        File file = new File("database.txt");
        Scanner fileScan = new Scanner(file);

        String correspondingPosition = "";

        while(fileScan.hasNextLine()) {

          String line   = fileScan.nextLine();
          String[] data = line.split(",");

          if (data[0].equals(positionNumber)) {
            data = Arrays.copyOfRange(data, 1, data.length);
            correspondingPosition = String.join(",", data);
            break;
          }
        }

        File applicationsFile = new File("applications.txt");
        applicationsFile.createNewFile();
        
        FileWriter myWriter = new FileWriter(applicationsFile.getName(), true);

        myWriter.write("\n"+profileObj.getProfile(loggedUser)+"/"+correspondingPosition);

        myWriter.close();
        fileScan.close();

        System.out.print("\nApplication submitted successfully!");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }
  }
}

class UserClass {

  String loggedUser;

  static String login() {
    Scanner enter = new Scanner(System.in);
    System.out.print("Enter your username: ");
    String username = enter.nextLine();

    System.out.print("Enter your ID number: ");
    int ID_Number = enter.nextInt();

    try {
      File myObj = new File("Users.txt");
      Scanner myReader = new Scanner(myObj);

      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        String[] parts = data.split(",");

        if (parts[0].equals(username)) {
          System.out.println("\nLogin successful");
          myReader.close();
          return parts[0];
        }
      }
      myReader.close();
      System.out.println("\nInvalid username or ID number");
      return "";
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return "";
  }

  static void signUp() {
    Scanner enter = new Scanner(System.in);
    System.out.print("Enter your username: ");
    String username = enter.nextLine();

    System.out.print("Enter your ID number: ");
    int ID_Number = enter.nextInt();

    try {

      File myObj = new File("Users.txt");
      myObj.createNewFile();

      FileWriter myWriter = new FileWriter("Users.txt", true);
      myWriter.write("\n" + username + "," + ID_Number);
      myWriter.close();

      System.out.println("\nSuccessfully saved.");
      System.out.println("\nThank you!");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}

class Profile {
  static void createStudentProfile(String loggedUser) {

    if (loggedUser == null || loggedUser == "") {
      System.out.println("\nLogin to create a profile");  
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.println("\nEnter the student profile details:\n");

      System.out.print("ID: ");
      int id = scanner.nextInt();
      scanner.nextLine(); // Consume newline character

      System.out.print("Name: ");
      String name = scanner.nextLine();

      System.out.print("Major: ");
      String major = scanner.nextLine();

      System.out.print("Email: ");
      String email = scanner.nextLine();

      System.out.print("GPA: ");
      double gpa = scanner.nextDouble();
      scanner.nextLine(); // Consume newline character

      System.out.print("Contact: ");
      String contact = scanner.nextLine();

      System.out.print("Skills: ");
      String skills = scanner.nextLine();

      String profile;
      profile = "\n"+loggedUser+","+name+","+major+","+email+","+gpa+","+contact+","+skills;

      try {
        FileWriter myWriter = new FileWriter("profiles.txt", true);
        myWriter.write(profile);
        myWriter.close();
        System.out.println("profile created");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }
  }

  static String getProfile(String loggedUser) {
    try {
      File myObj = new File("profiles.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data    = myReader.nextLine();
        String[] parts = data.split(",");

        if (parts[0].equals(loggedUser)) {
          System.out.println(parts[0]);
          return data;
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    return "";
  }
}

class file {

  public static void main(String[] args) {

    Menu currentMenu = new Menu();
    currentMenu.showMenu();
  }
}