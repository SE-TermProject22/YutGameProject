import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double totalOrderPrice = 0.0;

        // 주문 반복 입력 및 가격 계산
        while (true) {
            System.out.println("Please select the food you want to order (1: Burger, 2: Side, 3: Drink, 0: Finish ordering)");
            int choice = scanner.nextInt();
            if (choice == 0) {
                break;
            }

            Food selectedFood = null;
            switch (choice) {
                case 1:
                    selectedFood = new Burger();
                    System.out.println("Want to add cheese? (1: add, 0: do not add)");
                    int cheeseChoice = scanner.nextInt();
                    selectedFood.calculatePrice(cheeseChoice);

                    break;

                case 2:
                    selectedFood = new Side();
                    System.out.println("How many would you like to order?");
                    int quantity = scanner.nextInt();
                    //selectedFood.price = selectedFood.calculatePrice(quantity);
                    break;

                case 3:
                    selectedFood.name = "Soda";
                    Drink Drink = new Drink("Soda", 500);
                    System.out.println("Do you want a drink in 'Large' or 'Medium' size?");
                    String addtionalChoice = scanner.nextLine();
                    System.out.println(addtionalChoice);

                    if(addtionalChoice.equals("Medium"))
                    {
                        selectedFood.price = Drink.price;
                    }
                    else if(addtionalChoice.equals("Large"))
                    {
                        Drink.calculatePrice(Drink.price);
                        selectedFood.price = Drink.price;
                    }
                    break;

                default:
                    System.out.println("It's a wrong choice. Please select again.");
                    continue;
            }

            double foodPrice = selectedFood.calculatePrice();
            System.out.println("You selected " + selectedFood.name + ". Price: $" + foodPrice + "\n");
            totalOrderPrice += foodPrice;
        }

        // 총 주문 금액 출력
        System.out.println("The total price is " + totalOrderPrice + " won.");

        // Scanner 종료
        scanner.close();
    }
}