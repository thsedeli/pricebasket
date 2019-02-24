# pricebasket

Steps to run the application

- You  can download the file PriceBasket.class from the repository's root folder and run it through terminal using the following command:
  java PriceBasket PriceBasket Milk Apple Soup
  see more examples below 

- Alternatively :
1. Download and extract the repository
2. Create New Project from existing resources in Intellij IDEA and select the downloaded project
3. Select Import Project from external model, select Maven and click Next
4. Click Next, Next Yes and Finish in the next windows
5. Click Add Configuration in the top right
6. From the Templates choose Application and then choose the main class (PriceBasket)
7. Add Program Arguments which are the input params of the program
  - Examples : 
    - Example invalid input : Example Milk Apple
    - Example valid input 1 (with offers): PriceBasket Milk Apple Apple Soup Soup Soup Bread Bread
    - Example valid input 2 (no offers): PriceBasket Milk Bread Soup
  
8. In use classpath of module select price-basket
9. Run the main class and you'll see the output.
10. You can change the program arguments through the configuration.

Assumptions

I named the variable for apples as "Apple" which is a bag of Apples. I assume that each Apple coming in as an input is a bag of apples.

