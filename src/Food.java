public class Food {
    protected String name;
    protected double price;

    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double calculatePrice() {
        return price;
    }
}


class Burger extends Food {
    String name;
    public Burger() {
        super("Cheese Burger", 1500);
    }

    void calculatePrice(int choice)
    {
        super.calculatePrice();
        if(choice == 1)
        {
            this.price = price + 600;
        }

    }
}

class Side extends Food {
    String name;
    public Side() {
        super("Chicken Nugget", 1500);
    }
    double calculatePrice(int quantity)
    {
        super.calculatePrice();
        this.price = price * quantity;
        return this.price;
    }
}

class Drink extends Food {
    String name;
    double price = 500;
    public Drink(String name, double price) {
        super(name, price);
    }
    void calculatePrice(double price)
    {
        super.calculatePrice();
        this.price = price + 1000;
    }
}



