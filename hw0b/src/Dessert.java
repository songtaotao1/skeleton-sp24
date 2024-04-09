public class Dessert {
    int flavor;  // 实例变量
    int price;

    public Dessert(int flavor, int price) {
        this.flavor = flavor;  // 将构造函数的参数 flavor 赋值给实例变量 flavor
        this.price = price;    // 同样，构造函数的参数 price 赋值给实例变量 price
    }

    public void printDessert() {
        System.out.println("Flavor: " + flavor + ", Price: " + price);
    }

    public static void main(String[] args) {
        Dessert dessert1 = new Dessert(1, 2);  // 创建对象时传入参数 1 和 2
        dessert1.printDessert();  // 输出: Flavor: 1, Price: 2
    }
}
