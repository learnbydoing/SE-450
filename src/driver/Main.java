//package driver;
//
//import java.util.ArrayList;
//
//import tradable.Tradable.Side;
//import book.ProductService;
//import book.ProductBookSide.BookSide;
//import client.User;
//import client.UserImpl;
//
//import price.PriceFactory;
//import publishers.MarketMessage.MarketState;
//
//
//public class Main {
//
//    private static ArrayList<User> users = new ArrayList<User>();
//    private static int testCount = 1;
//
//    public static void main(String[] args) {
//        
//        try {
//            System.out.println("A) Add some stocks to the Trading System: IBM, CBOE, GOOG, AAPL, GE, T");
//            String[] products = {"IBM", "CBOE", "GOOG", "AAPL", "GE", "T"};
//            for (String prod : products) {
//                ProductService.getInstance().createProduct(prod);
//            }
//            System.out.println();
//
//            System.out.println("B) Create 3 users: REX, ANN, RAJ");
//            
//            users.add(new UserImpl("REX"));
//            users.add(new UserImpl("ANN"));
//            users.add(new UserImpl("RAJ"));
//            System.out.println();
//
//            System.out.println("C) Connect users REX, ANN, RAJ to the trading system");
//            for (int i = 0; i < 3; i++) {
//                users.get(i).connect();
//            }
//            System.out.println();
//
//            System.out.println("D) Subscribe users REX, ANN, RAJ to Current Market, Last Sale, Ticker, and Messages for all Stocks");
//            for (int i = 0; i < 3; i++) {
//                for (String prod : products) {
//                    users.get(i).subscribeCurrentMarket(prod);
//                    users.get(i).subscribeLastSale(prod);
//                    users.get(i).subscribeMessages(prod);
//                    users.get(i).subscribeTicker(prod);
//                }
//                users.get(i).showMarketDisplay();
//            }
//            System.out.println();
//
//            System.out.println("E) User REX queries market state");
//            System.out.println(users.get(0).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
//            System.out.println();
//
//            System.out.println("F) Put the market in PREOPEN state");
//            ProductService.getInstance().setMarketState(MarketState.PREOPEN);
//            System.out.println();
//
//            System.out.println("G) User ANN queries market state");
//            System.out.println(users.get(1).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
//            System.out.println();
//
//            System.out.println("H) Put the market in OPEN state");
//            ProductService.getInstance().setMarketState(MarketState.OPEN);
//            System.out.println();
//
//            System.out.println("I) User RAJ queries market state");
//            System.out.println(users.get(2).getUserName() + ": Market State Query: " + users.get(0).getMarketState());
//            System.out.println();
//            
//            runTests("GOOG");
//            runTests("IBM");
//            
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
//
//    private static void runTests(String stock) {
//        try {
//
//
//            System.out.println(testCount++ + ".1) User REX submits an order for " + stock + ", BUY 100@40.00");
//            String rexO1 = users.get(0).submitOrder(stock, PriceFactory.makeLimitPrice("$40.00"), 100, Side.BUY);
//            System.out.println();
//
//            System.out.println(testCount++ + ".2) User ANN submits a quote for " + stock + ", 100@40.00 x 100@40.50");
//            users.get(1).submitQuote(stock, PriceFactory.makeLimitPrice("$40.00"), 100, PriceFactory.makeLimitPrice("$40.50"), 100);
//            System.out.println();
//
//
//            System.out.println(testCount++ + ".3) User RAJ submits an Order for " + stock + ", SELL 135@40.50");
//            String o2 = users.get(2).submitOrder(stock, PriceFactory.makeLimitPrice("$40.50"), 135, Side.SELL);
//            System.out.println();
//
//            System.out.println(testCount++ + ".4) User REX does a Book Depth query for " + stock);
//            System.out.println("Book Depth: " + users.get(0).getBookDepth(stock)[0][0] + " -- " + users.get(0).getBookDepth(stock)[1][0]);
//            System.out.println();
//
//            System.out.println(testCount++ + ".5) User REX does a query for their orders with remaining quantity for " + stock);
//            System.out.println(users.get(0).getUserName() + " Orders With Remaining Qty: " + users.get(0).getOrdersWithRemainingQty(stock));
//            System.out.println();
//
//            System.out.println(testCount++ + ".6) User ANN does a query for their orders with remaining quantity for GE " + stock);
//            System.out.println(users.get(1).getUserName() + " Orders With Remaining Qty: " + users.get(1).getOrdersWithRemainingQty("GE"));
//            System.out.println();
//
//            System.out.println(testCount++ + ".7) User RAJ does a query for their orders with remaining quantity for GE " + stock);
//            System.out.println(users.get(2).getUserName() + " Orders With Remaining Qty: " + users.get(2).getOrdersWithRemainingQty(stock));
//            System.out.println();
//
//            System.out.println(testCount++ + ".8) User REX cancels their order");
//            users.get(0).submitOrderCancel(stock, BookSide.BUY, rexO1);
//            System.out.println();
//
//            System.out.println(testCount++ + ".9) User ANN cancels their quote");
//            users.get(1).submitQuoteCancel(stock);
//            System.out.println();
//
//            System.out.println(testCount++ + ".10) User RAJ cancels their order");
//            users.get(2).submitOrderCancel(stock, BookSide.SELL, o2);
//            System.out.println();
//
//            System.out.println(testCount++ + ".11) Display position values for all users");
//            for (User u : users) {
//                System.out.print(u.getUserName() + " Stock Value: " + u.getAllStockValue());
//                System.out.print(", Account Costs: " + u.getAccountCosts());
//                System.out.println(", Net Value: " + u.getNetAccountValue());
//            }
//            System.out.println();
//
//            System.out.println(testCount++ + ".12) User REX enters an order for " + stock + ", BUY 100@$10.00");
//            users.get(0).submitOrder(stock, PriceFactory.makeLimitPrice("$10.00"), 100, Side.BUY);
//            System.out.println();
//
//            System.out.println(testCount++ + ".13) User ANN enters a quote for " + stock + ", 100@$10.00 x 100@10.10");
//            users.get(1).submitQuote(stock, PriceFactory.makeLimitPrice("$10.00"), 100, PriceFactory.makeLimitPrice("$10.10"), 100);
//            System.out.println();
//
//            System.out.println(testCount++ + ".14) User RAJ enters an order for " + stock + ", SELL 150@$10.00 - results in a trade");
//            users.get(2).submitOrder(stock, PriceFactory.makeLimitPrice("$10.00"), 150, Side.SELL);
//            System.out.println();
//            
//            System.out.println(testCount++ + ".15) User REX does a Book Depth query for " + stock);
//            System.out.println("IBM Book Depth: " + users.get(0).getBookDepth(stock)[0][0] + " -- " + users.get(0).getBookDepth(stock)[1][0]);
//            System.out.println();
//
//            System.out.println(testCount++ + ".16) User REX enters a market order for " + stock + ", SELL 75@MKT - results in a trade");
//            users.get(0).submitOrder(stock, PriceFactory.makeMarketPrice(), 75, Side.BUY);
//            System.out.println();
//
//            System.out.println(testCount++ + ".17) User ANN does a Book Depth query for " + stock);
//            System.out.println("IBM Book Depth: " + users.get(1).getBookDepth(stock)[0][0] + " -- " + users.get(1).getBookDepth(stock)[1][0]);
//            System.out.println();
//
//            System.out.println(testCount++ + ".18) User ANN cancels her quote for "+ stock);
//            users.get(1).submitQuoteCancel(stock);
//            System.out.println();
//
//            System.out.println(testCount++ + ".19) Show stock holdings for all users");
//            System.out.println(users.get(0).getUserName() + " Holdings: " + users.get(0).getHoldings());
//            System.out.println(users.get(1).getUserName() + " Holdings: " + users.get(1).getHoldings());
//            System.out.println(users.get(2).getUserName() + " Holdings: " + users.get(2).getHoldings());
//            System.out.println();
//
//            System.out.println(testCount++ + ".20) Show order Id's  for all users");
//            System.out.println(users.get(0).getUserName() + " Orders: " + users.get(0).getOrderIds());
//            System.out.println(users.get(1).getUserName() + " Orders: " + users.get(1).getOrderIds());
//            System.out.println(users.get(2).getUserName() + " Orders: " + users.get(2).getOrderIds());
//            System.out.println();
//
//
//            System.out.println(testCount++ + ".21) Show positions for all users");
//            for (User u : users) {
//                System.out.print(u.getUserName() + " Stock Value: " + u.getAllStockValue());
//                System.out.print(", Account Costs: " + u.getAccountCosts());
//                System.out.println(", Net Value: " + u.getNetAccountValue());
//            }
//            System.out.println();
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
//}
//
//
////package driver;
////
////import java.util.ArrayList;
////
////import price.Price;
////import price.PriceFactory;
////import publishers.CancelMessage;
////import publishers.CurrentMarketPublisher;
////import publishers.FillMessage;
////import publishers.LastSalePublisher;
////import publishers.MarketMessage.MarketState;
////import publishers.MessagePublisher;
////import publishers.TickerPublisher;
////import tradable.Order;
////import tradable.Quote;
////import tradable.Tradable.Side;
////import utils.AlreadyConnectedException;
////import utils.AlreadySubscribedException;
////import utils.DataValidationException;
////import utils.InvalidArgumentException;
////import utils.InvalidConnectionIdException;
////import utils.InvalidMarketStateException;
////import utils.InvalidPriceOperation;
////import utils.InvalidVolumeValueException;
////import utils.NoSuchProductException;
////import utils.OrderNotFoundException;
////import utils.UserNotConnectedException;
////import book.ProductBookSide.BookSide;
////import book.ProductService;
////import client.TradableUserData;
////import client.User;
////import dto.TradableDTO;
//
//
//
///* YOU NEED TO ADD IMPORT STATEMENTS FOR YOUR CLASSES HERE   */
///* Mine were here but I removed them to not cause confusion. */
///* You will need to import any class used in this "main".    */
//
////import ....
////import ....
//
///**
// * You will need to make changes in this class to match your design. The class 
// * names and method names used here should be *close* to what
// * you have. It's ok to adjust this "main" to work with your classes.
// *
// * You cannot REMOVE tests, but you can feel free to make adjustments to match
// * your particular implementation. If you are unable to get this to compile and
// * run with your version of the assignment, email me the project and together we
// * can get it working.
// *
// */
//public class Main {
//
//    private static User u1, u2;
//
//    public static void main(String[] args) {
//
//        makeTestUsers();
//
//        doTradingScenarios("GOOG");
//
//        doBadTests("GOOG");
//    }
//
//    private static void doBadTests(String stockSymbol) {
//
//        try {
//            System.out.println("31) Change Market State to PREOPEN then to OPEN. Market messages received");
//            ProductService.getInstance().setMarketState(MarketState.PREOPEN);
//            ProductService.getInstance().setMarketState(MarketState.OPEN);
//            System.out.println();
//
//            System.out.println("32) User " + u1.getUserName() + " cancels a non-existent order ");
//            ProductService.getInstance().submitOrderCancel(stockSymbol, BookSide.BUY, "ABC123");
//            System.out.println("Did not catch non-existent Stock error!");
//
//        } 
//        catch (Exception ex) 
//        {
//            System.out.println("Properly caught error: " + ex.getMessage());
//            System.out.println();
//        }
//
//        try {
//            System.out.println("33) User " + u1.getUserName() + " cancels a non-existent quote ");
//            ProductService.getInstance().submitQuoteCancel(u1.getUserName(), stockSymbol);
//            System.out.println("Cancelling a non-existant quote does nothing as expected");
//            System.out.println();
//
//        } catch (Exception ex) {
//            System.out.println("Caught error that should not have occurred: " + ex.getMessage());
//        }
//        
//        try {
//            System.out.println("34) Try to create a bad product");
//            ProductService.getInstance().createProduct(null);
//        } catch (Exception ex) {
//            System.out.println("Caught error on bad product: " + ex.getMessage());
//            System.out.println();
//        } 
//        
//        try {
//            System.out.println("35) User " + u1.getUserName() + " enters order on non-existent stock");
//            ProductService.getInstance().submitOrder(new Order(u1.getUserName(), "X11", PriceFactory.makeLimitPrice("$641.10"), 111, Side.BUY));
//            System.out.println("Did not catch non-existent Stock error!");
//        } catch (Exception ex) {
//            System.out.println("Caught error on order for bad class: " + ex.getMessage());
//            System.out.println();
//        }
//        
//        try {
//            System.out.println("36) User " + u1.getUserName() + " enters quote on non-existent stock");
//            ProductService.getInstance().submitQuote(
//                    new Quote(u1.getUserName(), "X11", PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
//            System.out.println("Did not catch non-existent Stock error!");
//        } catch (Exception ex) {
//            System.out.println("Caught error on quote for bad class: " + ex.getMessage());
//            System.out.println();
//        }
//    }//end doBadTests
//
//    private static void doTradingScenarios(String stockSymbol) 
//    {
//        try {
//            System.out.println("1) Check the initial Market State:\n" + ProductService.getInstance().getMarketState() + "\n");
//
//            System.out.print("2) Create a new Stock product in our Trading System: " + stockSymbol);
//            ProductService.getInstance().createProduct(stockSymbol);
//
//            System.out.println(", then Query the ProductService for all Stock products:\n" + ProductService.getInstance().getProductList() + "\n");
//
//
//            System.out.println("3) Subscribe 2 test users for CurrentMarket, LastSale, Ticker & Messages");
//            CurrentMarketPublisher.getInstance().subscribe(u1, stockSymbol);
//            CurrentMarketPublisher.getInstance().subscribe(u2, stockSymbol);
//            LastSalePublisher.getInstance().subscribe(u1, stockSymbol);
//            LastSalePublisher.getInstance().subscribe(u2, stockSymbol);
//            TickerPublisher.getInstance().subscribe(u1, stockSymbol);
//            TickerPublisher.getInstance().subscribe(u2, stockSymbol);
//            MessagePublisher.getInstance().subscribe(u1, stockSymbol);
//            MessagePublisher.getInstance().subscribe(u2, stockSymbol);
//
//            System.out.println("   then change Market State to PREOPEN and verify the Market State...");
//            ProductService.getInstance().setMarketState(MarketState.PREOPEN);
//            System.out.println("Product State: " + ProductService.getInstance().getMarketState() + "\n");
//
//            System.out.println("4) User " + u1.getUserName() + " enters a quote, Current Market updates received by both users: ");
//            ProductService.getInstance().submitQuote(new Quote(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
//            System.out.println();
//
//            System.out.println("5) Verify Quote is in Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//            System.out.println("6) Get MarketDataDTO for " + stockSymbol + " (Your format might vary but the data content should be the same)");
//            System.out.println(ProductService.getInstance().getMarketData(stockSymbol));
//            System.out.println();
//
//            System.out.println("7) Cancel that Quote, Cancels and Current Market updated received");
//            ProductService.getInstance().submitQuoteCancel(u1.getUserName(), stockSymbol);
//            System.out.println();
//
//            System.out.println("8) Verify Quote is NOT in Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//            System.out.println("9) Get MarketDataDTO for " + stockSymbol);
//            System.out.println(ProductService.getInstance().getMarketData(stockSymbol));
//            System.out.println();
//
//
//            System.out.println("10) User " + u2.getUserName() + " enters a quote, Current Market received by both users: ");
//            ProductService.getInstance().submitQuote(
//                    new Quote(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 120, PriceFactory.makeLimitPrice("$641.15"), 150));
//            System.out.println();
//
//            System.out.println("11) User " + u1.getUserName() + " enters 5 BUY orders, Current Market updates received (10) by both users for each order: ");
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.10"), 111, Side.BUY));
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.11"), 222, Side.BUY));
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.12"), 333, Side.BUY));
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.13"), 444, Side.BUY));
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.14"), 555, Side.BUY));
//            System.out.println();
//
//            System.out.println("12) Verify Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//
//            System.out.println("13) User " + u2.getUserName() + " enters several 5 Sell orders - no Current Market received - does not improve the market: ");
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.16"), 111, Side.SELL));
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.17"), 222, Side.SELL));
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.18"), 333, Side.SELL));
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.19"), 444, Side.SELL));
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.20"), 555, Side.SELL));
//            System.out.println();
//
//            System.out.println("14) Verify Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//
//            System.out.println("15) User " + u2.getUserName() + " enters a BUY order that is tradable (Current Market received), but won't trade as market is in PREOPEN: ");
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("$641.15"), 105, Side.BUY));
//            System.out.println();
//
//            System.out.println("16) Change Market State to OPEN State...Trade should occur.");
//            System.out.println("    Both users should get Market Message, Fill Messages, Current Market, Last Sale & Tickers.");
//            ProductService.getInstance().setMarketState(MarketState.OPEN);
//            System.out.println();
//
//            System.out.println("17) Verify Book after the trade: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//            System.out.println("18) User " + u1.getUserName() + " enters a big MKT BUY order to trade with all the SELL side:");
//            System.out.println("    Both users should get many Fill Messages, as well as Current Market, Last Sale & Tickers - and a cancel for the unfilled volume");
//            ProductService.getInstance().submitOrder(new Order(u1.getUserName(), stockSymbol, PriceFactory.makeMarketPrice(), 1750, Side.BUY));
//            System.out.println();
//
//            System.out.println("19) Verify Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//            System.out.println("20) Get Orders with Remaining Quantity: ");
//            ArrayList<TradableDTO> ords = ProductService.getInstance().getOrdersWithRemainingQty(u1.getUserName(), stockSymbol);
//            for (TradableDTO dto : ords) 
//            {
//                System.out.println(dto);
//            }
//            System.out.println();
//
//            System.out.println("21) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.");
//            ProductService.getInstance().setMarketState(MarketState.CLOSED);
//            System.out.println();
//            
//            System.out.println("22) Verify Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//
//            System.out.println("23) Change Market State to PREOPEN then to OPEN. Market messages received");
//            ProductService.getInstance().setMarketState(MarketState.PREOPEN);
//            ProductService.getInstance().setMarketState(MarketState.OPEN);
//            System.out.println();
//
//            System.out.println("24) User " + u1.getUserName() + " enters a BUY order, Current Market received");
//            ProductService.getInstance().submitOrder(new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64130), 369, Side.BUY));
//            System.out.println();
//
//            System.out.println("25) User " + u2.getUserName() + " enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers");
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64130), 369, Side.SELL));
//            System.out.println();
//
//            System.out.println("26) User " + u1.getUserName() + " enters a MKT BUY order, cancelled as there is no market");
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeMarketPrice(), 456, Side.BUY));
//            System.out.println();
//
//            System.out.println("27) User " + u1.getUserName() + " enters a BUY order, Current Market received");
//            ProductService.getInstance().submitOrder(
//                    new Order(u1.getUserName(), stockSymbol, PriceFactory.makeLimitPrice("641.1"), 151, Side.BUY));
//            System.out.println();
//
//            System.out.println("28) User " + u2.getUserName() + " enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers");
//            ProductService.getInstance().submitOrder(
//                    new Order(u2.getUserName(), stockSymbol, PriceFactory.makeLimitPrice(64110), 51, Side.SELL));
//            System.out.println();
//            
//            System.out.println("29) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.");
//            ProductService.getInstance().setMarketState(MarketState.CLOSED);
//            System.out.println();
//
//            System.out.println("30) Verify Book: ");
//            printOutBD(ProductService.getInstance().getBookDepth(stockSymbol));
//            System.out.println();
//            
//
//
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
// }//end doTradingScenarios
//
//    private static void printOutBD(String[][] bd) {
//        String[] buy = bd[0];
//        String[] sell = bd[1];
//        System.out.println("Buy Side:");
//        for (String s : buy) {
//            System.out.println("\t" + s);
//        }
//        System.out.println("Sell Side:");
//        for (String s : sell) {
//            System.out.println("\t" + s);
//        }
//    }
//
//    private static void makeTestUsers() {
//        u1 = new UserImpl("REX");
//        u2 = new UserImpl("ANN");
//
//    }
//////////////////////////////////////////////////////////////////////////
//
//    static class UserImpl implements User {
//
//        private String uname;
//
//        public UserImpl(String u) {
//            uname = u;
//        }
//
//        @Override
//        public String getUserName() {
//            return uname;
//        }
//
//        @Override
//        public void acceptLastSale(String product, Price p, int v) {
//            System.out.println("User " + getUserName() + " Received Last Sale for " + product + " " + v + "@" + p);
//        }
//
//        @Override
//        public void acceptMessage(FillMessage fm) {
//            System.out.println("User " + getUserName() + " Received Fill Message: " + fm);
//        }
//
//        @Override
//        public void acceptMessage(CancelMessage cm) {
//            System.out.println("User " + getUserName() + " Received Cancel Message: " + cm);
//        }
//
//        @Override
//        public void acceptMarketMessage(String message) {
//            System.out.println("User " + getUserName() + " Received Market Message: " + message);
//        }
//
//        @Override
//        public void acceptTicker(String product, Price p, char direction) {
//            System.out.println("User " + getUserName() + " Received Ticker for " + product + " " + p + " " + direction);
//        }
//
//        @Override
//        public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv) {
//            System.out.println("User " + getUserName() + " Received Current Market for " + product + " " + bv + "@" + bp + " - " + sv + "@" + sp);
//        }
//
//		@Override
//		public void connect() throws InvalidArgumentException,
//				AlreadyConnectedException, UserNotConnectedException,
//				InvalidConnectionIdException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void disConnect() throws UserNotConnectedException,
//				InvalidConnectionIdException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void showMarketDisplay() throws UserNotConnectedException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public String submitOrder(String product, Price price, int volume,
//				Side side) throws InvalidMarketStateException,
//				NoSuchProductException, InvalidArgumentException,
//				InvalidVolumeValueException, UserNotConnectedException,
//				InvalidConnectionIdException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public void submitOrderCancel(String product, BookSide side,
//				String orderId) throws InvalidMarketStateException,
//				NoSuchProductException, InvalidArgumentException,
//				InvalidVolumeValueException, OrderNotFoundException,
//				UserNotConnectedException, InvalidConnectionIdException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void submitQuote(String product, Price buyPrice, int buyVolume,
//				Price sellPrice, int sellVolume)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				InvalidVolumeValueException, InvalidArgumentException,
//				InvalidMarketStateException, NoSuchProductException,
//				DataValidationException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void submitQuoteCancel(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				InvalidMarketStateException, NoSuchProductException,
//				InvalidArgumentException, InvalidVolumeValueException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void subscribeCurrentMarket(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				AlreadySubscribedException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void subscribeLastSale(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				AlreadySubscribedException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void subscribeMessages(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				AlreadySubscribedException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void subscribeTicker(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				AlreadySubscribedException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public Price getAllStockValue() throws InvalidPriceOperation,
//				InvalidArgumentException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Price getAccountCosts() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Price getNetAccountValue() throws InvalidPriceOperation,
//				InvalidArgumentException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String[][] getBookDepth(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				NoSuchProductException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getMarketState() throws UserNotConnectedException,
//				InvalidConnectionIdException, InvalidArgumentException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public ArrayList<TradableUserData> getOrderIds() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public ArrayList<String> getProductList() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public Price getStockPositionValue(String sym)
//				throws InvalidPriceOperation {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public int getStockPositionVolume(String product) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public ArrayList<String> getHoldings() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product)
//				throws UserNotConnectedException, InvalidConnectionIdException,
//				InvalidArgumentException {
//			// TODO Auto-generated method stub
//			return null;
//		}
//    }
//}


/*
1) Check the initial Market State:
CLOSED

2) Create a new Stock product in our Trading System: GOOG, then Query the ProductService for all Stock products:
[GOOG]

3) Subscribe 2 test users for CurrentMarket, LastSale, Ticker & Messages
   then change Market State to PREOPEN and verify the Market State...
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
Product State: PREOPEN

4) User REX enters a quote, Current Market updates received by both users: 
User ANN Received Current Market for GOOG 120@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 120@$641.10 - 150@$641.15

5) Verify Quote is in Book: 
Buy Side:
	$641.10 x 120
Sell Side:
	$641.15 x 150

6) Get MarketDataDTO for GOOG (Your format might vary but the data content should be the same)
Market Data [GOOG] 120@$641.10 x 150@$641.15

7) Cancel that Quote, Cancels and Current Market updated received
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 120, Details: Quote BUY-Side Cancelled, Side: BUY, Id: REXGOOG105982873160140
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.15, Volume: 150, Details: Quote SELL-Side Cancelled, Side: SELL, Id: REXGOOG105982873185932
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

8) Verify Quote is NOT in Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

9) Get MarketDataDTO for GOOG
Market Data [GOOG] 0@$0.00 x 0@$0.00

10) User ANN enters a quote, Current Market received by both users: 
User ANN Received Current Market for GOOG 120@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 120@$641.10 - 150@$641.15

11) User REX enters 5 BUY orders, Current Market updates received (10) by both users for each order: 
User ANN Received Current Market for GOOG 231@$641.10 - 150@$641.15
User REX Received Current Market for GOOG 231@$641.10 - 150@$641.15
User ANN Received Current Market for GOOG 222@$641.11 - 150@$641.15
User REX Received Current Market for GOOG 222@$641.11 - 150@$641.15
User ANN Received Current Market for GOOG 333@$641.12 - 150@$641.15
User REX Received Current Market for GOOG 333@$641.12 - 150@$641.15
User ANN Received Current Market for GOOG 444@$641.13 - 150@$641.15
User REX Received Current Market for GOOG 444@$641.13 - 150@$641.15
User ANN Received Current Market for GOOG 555@$641.14 - 150@$641.15
User REX Received Current Market for GOOG 555@$641.14 - 150@$641.15

12) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 150

13) User ANN enters several 5 Sell orders - no Current Market received - does not improve the market: 

14) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 150
	$641.16 x 111
	$641.17 x 222
	$641.18 x 333
	$641.19 x 444
	$641.20 x 555

15) User ANN enters a BUY order that is tradable (Current Market received), but won't trade as market is in PREOPEN: 
User ANN Received Current Market for GOOG 105@$641.15 - 150@$641.15
User REX Received Current Market for GOOG 105@$641.15 - 150@$641.15

16) Change Market State to OPEN State...Trade should occur.
    Both users should get Market Message, Fill Messages, Current Market, Last Sale & Tickers.
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 105, Details: leaving 0, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 105, Details: leaving 45, Side: SELL
User ANN Received Current Market for GOOG 555@$641.14 - 45@$641.15
User REX Received Current Market for GOOG 555@$641.14 - 45@$641.15
User ANN Received Last Sale for GOOG 105@$641.15
User REX Received Last Sale for GOOG 105@$641.15
User ANN Received Ticker for GOOG $641.15  
User REX Received Ticker for GOOG $641.15  

17) Verify Book after the trade: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	$641.15 x 45
	$641.16 x 111
	$641.17 x 222
	$641.18 x 333
	$641.19 x 444
	$641.20 x 555

18) User REX enters a big MKT BUY order to trade with all the SELL side:
    Both users should get many Fill Messages, as well as Current Market, Last Sale & Tickers - and a cancel for the unfilled volume
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.19, Fill Volume: 444, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.15, Fill Volume: 45, Details: leaving 1705, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.16, Fill Volume: 111, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.16, Fill Volume: 111, Details: leaving 1594, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.20, Fill Volume: 555, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.20, Fill Volume: 555, Details: leaving 40, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.17, Fill Volume: 222, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.17, Fill Volume: 222, Details: leaving 1372, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.15, Fill Volume: 45, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.18, Fill Volume: 333, Details: leaving 1039, Side: BUY
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.19, Fill Volume: 444, Details: leaving 595, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.18, Fill Volume: 333, Details: leaving 0, Side: SELL
User ANN Received Current Market for GOOG 555@$641.14 - 0@$0.00
User REX Received Current Market for GOOG 555@$641.14 - 0@$0.00
User ANN Received Last Sale for GOOG 1710@$641.15
User REX Received Last Sale for GOOG 1710@$641.15
User ANN Received Ticker for GOOG $641.15 =
User REX Received Ticker for GOOG $641.15 =
User REX Received Cancel Message: User: REX, Product: GOOG, Price: MKT, Volume: 40, Details: Cancelled, Side: BUY, Id: REXGOOGMKT105982897051413

19) Verify Book: 
Buy Side:
	$641.14 x 555
	$641.13 x 444
	$641.12 x 333
	$641.11 x 222
	$641.10 x 231
Sell Side:
	<Empty>

20) Get Orders with Remaining Quantity: 
Product: GOOG, Price: $641.14, OriginalVolume: 555, RemainingVolume: 555, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.14105982888527284
Product: GOOG, Price: $641.13, OriginalVolume: 444, RemainingVolume: 444, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.13105982887739275
Product: GOOG, Price: $641.12, OriginalVolume: 333, RemainingVolume: 333, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.12105982886980138
Product: GOOG, Price: $641.11, OriginalVolume: 222, RemainingVolume: 222, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.11105982886213301
Product: GOOG, Price: $641.10, OriginalVolume: 111, RemainingVolume: 111, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$641.10105982885392185

21) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.
User REX Received Market Message: CLOSED
User ANN Received Market Message: CLOSED
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.14, Volume: 555, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.14105982888527284
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.13, Volume: 444, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.13105982887739275
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.12, Volume: 333, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.12105982886980138
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.11, Volume: 222, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.11105982886213301
User ANN Received Cancel Message: User: ANN, Product: GOOG, Price: $641.10, Volume: 120, Details: Quote BUY-Side Cancelled, Side: BUY, Id: ANNGOOG105982883377314
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 111, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.10105982885392185
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

22) Verify Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

23) Change Market State to PREOPEN then to OPEN. Market messages received
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN

24) User REX enters a BUY order, Current Market received
User ANN Received Current Market for GOOG 369@$641.30 - 0@$0.00
User REX Received Current Market for GOOG 369@$641.30 - 0@$0.00

25) User ANN enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.30, Fill Volume: 369, Details: leaving 0, Side: BUY
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.30, Fill Volume: 369, Details: leaving 0, Side: SELL
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00
User ANN Received Last Sale for GOOG 369@$641.30
User REX Received Last Sale for GOOG 369@$641.30
User ANN Received Ticker for GOOG $641.30 
User REX Received Ticker for GOOG $641.30 

26) User REX enters a MKT BUY order, cancelled as there is no market
User REX Received Cancel Message: User: REX, Product: GOOG, Price: MKT, Volume: 456, Details: Cancelled, Side: BUY, Id: REXGOOGMKT105982905228309

27) User REX enters a BUY order, Current Market received
User ANN Received Current Market for GOOG 151@$641.10 - 0@$0.00
User REX Received Current Market for GOOG 151@$641.10 - 0@$0.00

28) User ANN enters a SELL order, users receive Fill Messages, as well as Current Market, Last Sale & Tickers
User ANN Received Fill Message: User: ANN, Product: GOOG, Fill Price: $641.10, Fill Volume: 51, Details: leaving 0, Side: SELL
User REX Received Fill Message: User: REX, Product: GOOG, Fill Price: $641.10, Fill Volume: 51, Details: leaving 100, Side: BUY
User ANN Received Current Market for GOOG 100@$641.10 - 0@$0.00
User REX Received Current Market for GOOG 100@$641.10 - 0@$0.00
User ANN Received Last Sale for GOOG 51@$641.10
User REX Received Last Sale for GOOG 51@$641.10
User ANN Received Ticker for GOOG $641.10 
User REX Received Ticker for GOOG $641.10 

29) Change Market State to CLOSED State...Both users should get a Market message, many Cancel Messages, and a Current Market Update.
User REX Received Market Message: CLOSED
User ANN Received Market Message: CLOSED
User REX Received Cancel Message: User: REX, Product: GOOG, Price: $641.10, Volume: 100, Details: BUY Order Cancelled, Side: BUY, Id: REXGOOG$641.10105982905384602
User ANN Received Current Market for GOOG 0@$0.00 - 0@$0.00
User REX Received Current Market for GOOG 0@$0.00 - 0@$0.00

30) Verify Book: 
Buy Side:
	<Empty>
Sell Side:
	<Empty>

31) Change Market State to PREOPEN then to OPEN. Market messages received
User REX Received Market Message: PREOPEN
User ANN Received Market Message: PREOPEN
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN

32) User REX cancels a non-existent order 
Properly caught error: Cannot find order ABC123

33) User REX cancels a non-existent quote 
Cancelling a non-existant quote does nothing as expected

34) Try to create a bad product
Caught error on bad product: Null or empty Stock Symbol passed to ProductService createProduct.

35) User REX enters order on non-existent stock
Caught error on order for bad class: Product X11 does not exist

36) User REX enters quote on non-existent stock
Caught error on quote for bad class: Product X11 does not exist

BUILD SUCCESSFUL (total time: 1 second)

 */







//
//import price.Price;
//import price.PriceFactory;
//import tradable.Tradable.Side;
//import dto.MarketDataDTO;
//import client.User;
//import publishers.CancelMessage;
//import publishers.CurrentMarketPublisher;
//import publishers.FillMessage;
//import publishers.LastSalePublisher;
//import publishers.MarketMessage;
//import publishers.MessagePublisher;
//import publishers.TickerPublisher;
//
//
//
///**
// * You might need to make changes in this "main" and related methods to match your exact type names especially
// * if you used interfaces and implementations as I did in some cases. You might also need to use different import
// * statements to match your code as well. That is fine. You cannot REMOVE tests but you can feel free to make 
// * adjustments to match your particular implementation. If you are unable to get this to compile and run with 
// * your version of the assignment, email me the project and together we can get it working.
// * 
// * @author hieldc
// */
//
//
///** 
// * This is the driver class for the application. It subscribes and unscribes to various publishers 
// * and requests the information provided by a given publisher to be sent out to the user.  
// * 
// * @author Urvi
// *
// */
//public class Main {
//
//    private static User u1, u2, u3, u4;
//
//    public static void main(String[] args) 
//    {
//        performLegacyTests();
//
//        makeTestUsers();
//		testCurrentMarketPublisher();
//        testTickerPublisher();
//        testLastSalePublisher();
//        testMessagePublisher();
//    }
//
//    private static void testMessagePublisher() {
//
//        try {
//            MessagePublisher.getInstance().subscribe(u1, "SBUX");  //REX
//            MessagePublisher.getInstance().subscribe(u2, "SBUX");  //ANN
//            MessagePublisher.getInstance().subscribe(u3, "SBUX");  //OWL
//            MessagePublisher.getInstance().subscribe(u4, "SBUX");  //BEN
//            
////            MessagePublisher.getInstance().subscribe(u1, "SBUX");
////            MessagePublisher.getInstance().unSubscribe(u2, "AAPL");
//
//            System.out.println("17) Send a CancelMessage to REX. REX is subscribed for SBUX messages so REX gets the message.");
//            CancelMessage cm = new CancelMessage("REX", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm);
//            System.out.println();
//            
//            System.out.println("17a) Send a CancelMessage to ANN. ANN is subscribed for SBUX messages so ANN gets the message.");
//            CancelMessage cm17a = new CancelMessage("ANN", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm17a);
//            System.out.println();
//            
//            System.out.println("17b) Send a CancelMessage to OWL. OWL is subscribed for SBUX messages so OWL gets the message.");
//            CancelMessage cm17b = new CancelMessage("OWL", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm17b);
//            System.out.println();
//            
//            MessagePublisher.getInstance().subscribe(u1, "AMZN");
//            
//            System.out.println("17c) Send a CancelMessage to REX. REX is subscribed for AMZN messages so REX gets the message.");
//            CancelMessage cm17c = new CancelMessage("REX", "AMZN", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm17c);
//            System.out.println();
//            
//            
//            System.out.println("18) Send a CancelMessage to REX. REX is NOT subscribed for IBM messages so REX does not get the message.");
//            CancelMessage cm2 = new CancelMessage("REX", "IBM", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm2);
//            System.out.println();
//                        
//            System.out.println("19) Send a CancelMessage to ANN. ANN is subscribed for SBUX messages so ANN gets the message.");
//            CancelMessage cm3 = new CancelMessage("ANN", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm3);
//            System.out.println();
//            
//            System.out.println("20) Send a CancelMessage to ANN. ANN is NOT subscribed for IBM messages so ANN does not get the message.");
//            CancelMessage cm4 = new CancelMessage("ANN", "IBM", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishCancel(cm4);
//            System.out.println();
//            
//            System.out.println("21) Send a FillMessage to REX. REX is subscribed for SBUX messages so REX gets the message.");
//            FillMessage fm = new FillMessage("REX", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishFill(fm);
//            System.out.println();
//            
//            System.out.println("22) Send a FillMessage to REX. REX is NOT subscribed for IBM messages so REX does not get the message.");
//            FillMessage fm2 = new FillMessage("REX", "IBM", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishFill(fm2);
//            System.out.println();
//                        
//            System.out.println("23) Send a FillMessage to ANN. ANN is subscribed for SBUX messages so ANN gets the message.");
//            FillMessage fm3 = new FillMessage("ANN", "SBUX", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishFill(fm3);
//            System.out.println();
//            
//            System.out.println("24) Send a FillMessage to ANN. ANN is NOT subscribed for IBM messages so ANN does not get the message.");
//            FillMessage fm4 = new FillMessage("ANN", "IBM", PriceFactory.makeLimitPrice("52.00"), 140, "Cancelled By User", Side.BUY, "ABC123XYZ");
//            MessagePublisher.getInstance().publishFill(fm4);
//            System.out.println(); 
//                      
//            MessagePublisher.getInstance().unSubscribe(u2, "SBUX");
//            System.out.println("25) Send a MarketMessage. REX is the only MessagePublisher subscriber so only REX gets the message.");
//            MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(MarketMessage.MarketState.PREOPEN));
//            //MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(GlobalConstants.MarketState.PREOPEN));
//            System.out.println(); 
//            
//            MessagePublisher.getInstance().subscribe(u2, "SBUX");
//            System.out.println("26) Send a MarketMessage. REX and ANN are MessagePublisher subscribers so REX & Ann get the message.");
//            MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(MarketMessage.MarketState.OPEN));
//            System.out.println(); 
//            
//            MessagePublisher.getInstance().unSubscribe(u1, "SBUX");
//            MessagePublisher.getInstance().unSubscribe(u2, "SBUX");
//            System.out.println("27) Send a MarketMessage. No users are MessagePublisher subscribers so No users get the message.");
//            //MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(GlobalConstants.MarketState.CLOSED));
//            MessagePublisher.getInstance().publishMarketMessage(new MarketMessage(MarketMessage.MarketState.CLOSED));
//            System.out.println("Done with Message tests\n"); 
//
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
//
//    private static void testLastSalePublisher() {
//        try {
//            LastSalePublisher.getInstance().subscribe(u1, "SBUX"); //REX
//            LastSalePublisher.getInstance().subscribe(u2, "IBM"); //ANN
//
//
// //         LastSalePublisher.getInstance().subscribe(u2, "IBM");  //OWL
// //         LastSalePublisher.getInstance().unSubscribe(u4, "AMZN");  //BEN
//            
//            System.out.println("12) Publish Last Sale for SBUX. Only user REX is subscribed, only REX gets a message:");
//            LastSalePublisher.getInstance().publishLastSale("SBUX", PriceFactory.makeLimitPrice("51.00"), 120);
//            System.out.println();
//            
//            System.out.println("13) Publish Last Sale for IBM. Only user ANN is subscribed, only ANN gets a message:");
//            LastSalePublisher.getInstance().publishLastSale("IBM", PriceFactory.makeLimitPrice("205.85"), 300);
//            System.out.println();
//            
//            LastSalePublisher.getInstance().subscribe(u3, "AAPL");  //OWL
//            LastSalePublisher.getInstance().subscribe(u4, "AAPL");  //BEN
//            
//            System.out.println("13a) Publish Last Sale for AAPL. OWL and BEN are subscribed, OWL and BEN a message:");
//            LastSalePublisher.getInstance().publishLastSale("AAPL", PriceFactory.makeLimitPrice("205.85"), 300);
//            System.out.println();
//            
//            LastSalePublisher.getInstance().subscribe(u1, "ALU");
//            
//            System.out.println("13b) Publish Last Sale for ALU. Only user REX is subscribed, only REX gets a message:");
//            LastSalePublisher.getInstance().publishLastSale("ALU", PriceFactory.makeLimitPrice(".70"), 40);
//            System.out.println();
//            
//           
//            LastSalePublisher.getInstance().subscribe(u2, "ALU");
//            
//            System.out.println("13c) Publish Last Sale for ALU. REX and ANN are subscribed, REX and ANN get a message:");
//            LastSalePublisher.getInstance().publishLastSale("ALU", PriceFactory.makeLimitPrice(".60"), 40);
//            System.out.println();
//            
//            LastSalePublisher.getInstance().unSubscribe(u1, "ALU");
//            
//            System.out.println("13d) Publish Last Sale for ALU. Only user ANN is subscribed, only ANN gets a message:");
//            LastSalePublisher.getInstance().publishLastSale("ALU", PriceFactory.makeLimitPrice(".55"), 40);
//            System.out.println();
//            
//            System.out.println("14) Publish Last Sale for GE. No user is subscribed, no user gets a message:");
//            LastSalePublisher.getInstance().publishLastSale("GE", PriceFactory.makeLimitPrice("22.70"), 40);
//            System.out.println();
//
//            LastSalePublisher.getInstance().subscribe(u2, "SBUX");
//            LastSalePublisher.getInstance().subscribe(u3, "SBUX");
//            LastSalePublisher.getInstance().subscribe(u4, "SBUX");
//
//            System.out.println("15) Publish Last Sale for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message");
//            LastSalePublisher.getInstance().publishLastSale("SBUX", PriceFactory.makeLimitPrice("51.00"), 120);
//            System.out.println();
//
//            LastSalePublisher.getInstance().unSubscribe(u1, "SBUX");
//            LastSalePublisher.getInstance().unSubscribe(u2, "SBUX");
//            LastSalePublisher.getInstance().unSubscribe(u3, "SBUX");
//            LastSalePublisher.getInstance().unSubscribe(u4, "SBUX");
//
//            System.out.println("16) Publish Last Sale for SBUX. Now all 4 users are unsubscribed so no one get a message");
//            LastSalePublisher.getInstance().publishLastSale("SBUX", PriceFactory.makeLimitPrice("51.00"), 120);
//            System.out.println("Done with Last Sale tests\n");
//
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
////
//    private static void testTickerPublisher() 
//    {
//        try {
//            TickerPublisher.getInstance().subscribe(u1, "SBUX"); //REX
//            TickerPublisher.getInstance().subscribe(u2, "IBM");  //ANN
//            
//            
//            
//            TickerPublisher.getInstance().subscribe(u3, "AAPL"); //OWL
//            TickerPublisher.getInstance().subscribe(u4, "AAPL");  //BEN
//            
////            TickerPublisher.getInstance().unSubscribe(u1, "SBUX"); //OWL
////            TickerPublisher.getInstance().subscribe(u4, "IBM");  //BEN
//
//            System.out.println("7) Publish Ticker for SBUX. Only user REX is subscribed, only REX gets a message:");
//            TickerPublisher.getInstance().publishTicker("SBUX", PriceFactory.makeLimitPrice("52.00"));
//            System.out.println();
//
//            TickerPublisher.getInstance().subscribe(u3, "SBUX"); //OWL
//            
//            System.out.println("7a) Publish Ticker for SBUX. REX and OWL are subscribed, REX and OWL get a message:");
//            TickerPublisher.getInstance().publishTicker("SBUX", PriceFactory.makeLimitPrice("56.00"));
//            System.out.println();
//            
//            System.out.println("7b) Publish Ticker for AAPL. OWL and BEN are subscribed, OWL and BEN get a message::");
//            TickerPublisher.getInstance().publishTicker("AAPL", PriceFactory.makeLimitPrice("715.12"));
//            System.out.println();
//            
//            TickerPublisher.getInstance().subscribe(u4, "IBM");  //BEN
//            
//            System.out.println("7c) Publish Ticker for IBM. ANN and BEN are subscribed, ANN and BEN get a message::");
//            TickerPublisher.getInstance().publishTicker("IBM", PriceFactory.makeLimitPrice("715.12"));
//            System.out.println();
//            
//            TickerPublisher.getInstance().unSubscribe(u4, "IBM");  //BEN
//            
//            System.out.println("8) Publish Ticker for IBM. Only user ANN is subscribed, only ANN gets a message:");
//            TickerPublisher.getInstance().publishTicker("IBM", PriceFactory.makeLimitPrice("204.85"));
//            System.out.println();
//
//            System.out.println("9) Publish Ticker for GE. No user is subscribed, no user gets a message:");
//            TickerPublisher.getInstance().publishTicker("GE", PriceFactory.makeLimitPrice("22.70"));
//            System.out.println();
//
//            TickerPublisher.getInstance().unSubscribe(u3, "SBUX");
//            
//            TickerPublisher.getInstance().subscribe(u2, "SBUX");
//            TickerPublisher.getInstance().subscribe(u3, "SBUX");
//            TickerPublisher.getInstance().subscribe(u4, "SBUX");
//
//            System.out.println("10) Publish Last Sale for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message");
//            TickerPublisher.getInstance().publishTicker("SBUX", PriceFactory.makeLimitPrice("52.10"));
//            System.out.println();
//
//            TickerPublisher.getInstance().unSubscribe(u1, "SBUX");
//            TickerPublisher.getInstance().unSubscribe(u2, "SBUX");
//            TickerPublisher.getInstance().unSubscribe(u3, "SBUX");
//            TickerPublisher.getInstance().unSubscribe(u4, "SBUX");
//
//            System.out.println("11) Publish Last Sale for SBUX. Now all 4 users are unsubscribed so no one get a message");
//            TickerPublisher.getInstance().publishTicker("SBUX", PriceFactory.makeLimitPrice("52.12"));
//            System.out.println();
//            
//            TickerPublisher.getInstance().subscribe(u1, "SBUX");
//            
//            System.out.println("12) Publish Ticker for SBUX. Only user REX is subscribed, only REX gets a message:");
//            TickerPublisher.getInstance().publishTicker("SBUX", PriceFactory.makeLimitPrice("52.23"));
//            System.out.println();
//            
//            TickerPublisher.getInstance().subscribe(u1, "ALU");
//            TickerPublisher.getInstance().subscribe(u2, "ALU");
//            
//            System.out.println("Done with Ticker tests\n");
//
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
//
//    private static void testCurrentMarketPublisher() {
//        try {
//            CurrentMarketPublisher.getInstance().subscribe(u1, "SBUX");  //REX
//            CurrentMarketPublisher.getInstance().subscribe(u2, "IBM");  //ANN
//            CurrentMarketPublisher.getInstance().subscribe(u3, "AAPL");  //OWL
//            
//            
// //           CurrentMarketPublisher.getInstance().subscribe(u3, "AMZN");
//            //CurrentMarketPublisher.getInstance().unSubscribe(u3, "AAPL");
//            
//
//            System.out.println("2) Publish Current Market for SBUX. Only user REX is subscribed, only REX gets a message:");
//            MarketDataDTO mdo = new MarketDataDTO("SBUX", PriceFactory.makeLimitPrice("51.00"), 120, PriceFactory.makeLimitPrice("51.06"), 75);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo);
//            System.out.println();
//
//            System.out.println("3) Publish Current Market for IBM. Only user ANN is subscribed, only ANN gets a message:");
//            MarketDataDTO mdo2 = new MarketDataDTO("IBM", PriceFactory.makeLimitPrice("205.85"), 300, PriceFactory.makeLimitPrice("205.98"), 220);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo2);
//            System.out.println();
//            
//            CurrentMarketPublisher.getInstance().subscribe(u4, "IBM");  //BEN
//            System.out.println("3a) Publish Current Market for IBM. Users ANN and BEN are subscribed, ANN and BEN get a message.");
//            MarketDataDTO mdo3a = new MarketDataDTO("IBM", PriceFactory.makeLimitPrice("205.85"), 300, PriceFactory.makeLimitPrice("205.98"), 220);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo3a);
//            System.out.println();
//
//            System.out.println("4) Publish Current Market for GE. No user is subscribed, no user gets a message:");
//            MarketDataDTO mdo3 = new MarketDataDTO("GE", PriceFactory.makeLimitPrice("22.70"), 40, PriceFactory.makeLimitPrice("22.78"), 110);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo3);
//            System.out.println();
//
//            CurrentMarketPublisher.getInstance().subscribe(u2, "SBUX");
//            CurrentMarketPublisher.getInstance().subscribe(u3, "SBUX");
//            CurrentMarketPublisher.getInstance().subscribe(u4, "SBUX");
//
//            System.out.println("5) Publish Current Market for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message");
//            MarketDataDTO mdo4 = new MarketDataDTO("SBUX", PriceFactory.makeLimitPrice("51.04"), 225, PriceFactory.makeLimitPrice("51.12"), 117);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo4);
//            System.out.println();
//
//            CurrentMarketPublisher.getInstance().unSubscribe(u1, "SBUX");
//            CurrentMarketPublisher.getInstance().unSubscribe(u2, "SBUX");
//            CurrentMarketPublisher.getInstance().unSubscribe(u3, "SBUX");
//            CurrentMarketPublisher.getInstance().unSubscribe(u4, "SBUX");
//
//            System.out.println("6) Publish Current Market for SBUX. Now all 4 users are unsubscribed so no one get a message");
//            MarketDataDTO mdo5 = new MarketDataDTO("SBUX", PriceFactory.makeLimitPrice("51.10"), 110, PriceFactory.makeLimitPrice("51.13"), 120);
//            CurrentMarketPublisher.getInstance().publishCurrentMarket(mdo5);
//            System.out.println("Done with Current Market tests\n");
//
//        } catch (Exception ex) {
//            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        	System.out.println(ex.getMessage());
//        }
//    }
//
//    private static void makeTestUsers() {
//        u1 = new UserImpl("REX");
//        u2 = new UserImpl("ANN");
//        u3 = new UserImpl("OWL");
//        u4 = new UserImpl("BEN");
//    }
//
//    private static void performLegacyTests() {
//
//        Price p1 = PriceFactory.makeLimitPrice("15.00");
//        Price p2 = PriceFactory.makeLimitPrice("$15.00");
//        Price p3 = PriceFactory.makeLimitPrice("15");
//
//        // Insure Flyweight functionality
//        System.out.println("1) The Strings '15.00 and '$15.00' and '15' should all result in the same Price object holding a long of 1500");
//        boolean r = p1 == p2;
//        System.out.println("\tIs 'p1'(15.00) the same Price object as 'p2' ($15.00): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//        r = p1 == p3;
//        System.out.println("\tIs 'p1'(15.00) the same Price object as 'p3' (15): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//        r = p2 == p3;
//        System.out.println("\tIs 'p2'($15.00) the same Price object as 'p3' (15): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//        r = p1 == p1;
//        System.out.println("\tIs 'p1'(15.00) the same Price object as 'p1' (15.00): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//        r = p2 == p2;
//        System.out.println("\tIs 'p2'($15.00) the same Price object as 'p2' ($15.00): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//        r = p3 == p3;
//        System.out.println("\tIs 'p3'(15) the same Price object as 'p3' (15): " + r + " (" + (r ? "CORRECT" : "ERROR") + ")");
//
//        System.out.println("Done with legacy tests\n");
//    }
//
//    static class UserImpl implements User {
//
//        private String uname;
//
//        public UserImpl(String u) {
//            uname = u;
//        }
//
//        @Override
//        public String getUserName() {
//            return uname;
//        }
//
//        @Override
//        public void acceptLastSale(String product, Price p, int v) {
//            System.out.println("User " + getUserName() + " Received Last Sale for " + product + " " + v + "@" + p);
//        }
//
//        @Override
//        public void acceptMessage(FillMessage fm) {
//            System.out.println("User " + getUserName() + " Received Fill Message: " + fm);
//        }
//
//        @Override
//        public void acceptMessage(CancelMessage cm) {
//            System.out.println("User " + getUserName() + " Received Cancel Message: " + cm);
//        }
//
//        @Override
//        public void acceptMarketMessage(String message) {
//            System.out.println("User " + getUserName() + " Received Market Message: " + message);
//        }
//
//        @Override
//        public void acceptTicker(String product, Price p, char direction) {
//            System.out.println("User " + getUserName() + " Received Ticker for " + product + " " + p + " " + direction);
//        }
//
//        @Override
//        public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv) {
//            System.out.println("User " + getUserName() + " Received Current Market for " + product + " " + bv + "@" + bp + " - " + sv + "@" + sp);
//        }
//        
//        public String toString()
//        {
//        	return getUserName();
//        }
//    }
//}


/*
1) The Strings '15.00 and '$15.00' and '15' should all result in the same Price object holding a long of 1500
	Is 'p1'(15.00) the same Price object as 'p2' ($15.00): true (CORRECT)
	Is 'p1'(15.00) the same Price object as 'p3' (15): true (CORRECT)
	Is 'p2'($15.00) the same Price object as 'p3' (15): true (CORRECT)
	Is 'p1'(15.00) the same Price object as 'p1' (15.00): true (CORRECT)
	Is 'p2'($15.00) the same Price object as 'p2' ($15.00): true (CORRECT)
	Is 'p3'(15) the same Price object as 'p3' (15): true (CORRECT)
Done with legacy tests

2) Publish Current Market for SBUX. Only user REX is subscribed, only REX gets a message:
User REX Received Current Market for SBUX 120@$51.00 - 75@$51.06

3) Publish Current Market for IBM. Only user ANN is subscribed, only ANN gets a message:
User ANN Received Current Market for IBM 300@$205.85 - 220@$205.98

4) Publish Current Market for GE. No user is subscribed, no user gets a message:

5) Publish Current Market for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message
User ANN Received Current Market for SBUX 225@$51.04 - 117@$51.12
User REX Received Current Market for SBUX 225@$51.04 - 117@$51.12
User BEN Received Current Market for SBUX 225@$51.04 - 117@$51.12
User OWL Received Current Market for SBUX 225@$51.04 - 117@$51.12

6) Publish Current Market for SBUX. Now all 4 users are unsubscribed so no one get a message
Done with Current Market tests

7) Publish Ticker for SBUX. Only user REX is subscribed, only REX gets a message:
User REX Received Ticker for SBUX $52.00  

8) Publish Ticker for IBM. Only user ANN is subscribed, only ANN gets a message:
User ANN Received Ticker for IBM $204.85  

9) Publish Ticker for GE. No user is subscribed, no user gets a message:

10) Publish Last Sale for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message
User ANN Received Ticker for SBUX $52.10 >
User REX Received Ticker for SBUX $52.10 >
User BEN Received Ticker for SBUX $52.10 >
User OWL Received Ticker for SBUX $52.10 >

11) Publish Last Sale for SBUX. Now all 4 users are unsubscribed so no one get a message
Done with Ticker tests

12) Publish Last Sale for SBUX. Only user REX is subscribed, only REX gets a message:
User REX Received Last Sale for SBUX 120@$51.00

13) Publish Last Sale for IBM. Only user ANN is subscribed, only ANN gets a message:
User ANN Received Last Sale for IBM 300@$205.85
User ANN Received Ticker for IBM $205.85 >

14) Publish Last Sale for GE. No user is subscribed, no user gets a message:

15) Publish Last Sale for SBUX. Now all 4 users are subscribed so REX, ANN, OWL & BEN get a message
User ANN Received Last Sale for SBUX 120@$51.00
User REX Received Last Sale for SBUX 120@$51.00
User BEN Received Last Sale for SBUX 120@$51.00
User OWL Received Last Sale for SBUX 120@$51.00

16) Publish Last Sale for SBUX. Now all 4 users are unsubscribed so no one get a message
Done with Last Sale tests

17) Send a CancelMessage to REX. REX is subscribed for SBUX messages so REX gets the message.
User REX Received Cancel Message: User: REX, Product: SBUX, Price: $52.00, Volume: 140, Details: Cancelled By User, Side: BUY, Id: ABC123XYZ

18) Send a CancelMessage to REX. REX is NOT subscribed for IBM messages so REX does not get the message.

19) Send a CancelMessage to ANN. ANN is subscribed for SBUX messages so ANN gets the message.
User ANN Received Cancel Message: User: ANN, Product: SBUX, Price: $52.00, Volume: 140, Details: Cancelled By User, Side: BUY, Id: ABC123XYZ

20) Send a CancelMessage to ANN. ANN is NOT subscribed for IBM messages so ANN does not get the message.

21) Send a FillMessage to REX. REX is subscribed for SBUX messages so REX gets the message.
User REX Received Fill Message: User: REX, Product: SBUX, Fill Price: $52.00, Fill Volume: 140, Details: Cancelled By User, Side: BUY

22) Send a FillMessage to REX. REX is NOT subscribed for IBM messages so REX does not get the message.

23) Send a FillMessage to ANN. ANN is subscribed for SBUX messages so ANN gets the message.
User ANN Received Fill Message: User: ANN, Product: SBUX, Fill Price: $52.00, Fill Volume: 140, Details: Cancelled By User, Side: BUY

24) Send a FillMessage to ANN. ANN is NOT subscribed for IBM messages so ANN does not get the message.

25) Send a MarketMessage. REX is the only MessagePublisher subscriber so only REX gets the message.
User REX Received Market Message: PREOPEN

26) Send a MarketMessage. REX and ANN are MessagePublisher subscribers so REX & Ann get the message.
User REX Received Market Message: OPEN
User ANN Received Market Message: OPEN

27) Send a MarketMessage. No users are MessagePublisher subscribers so No users get the message.
Done with Message tests
 */ //End of Driver - Phase 3



/*package org.se450.driver;

import org.se450.phase1.PriceFactory;
import org.se450.phase2.Order;
import org.se450.phase2.Quote;
import org.se450.phase2.Tradable;
import org.se450.phase2.Tradable.Side;
import org.se450.phase2.TradableDTO;
import org.se450.utils.*;

/* NOTE - Since the BUY & SELL indicators are being represented by you all in a variety of
 * ways (constants, enum types, etc), I cannot code this driver to use one specific method of representation.
 * 
 * So to account for that - I have put the text [BUY] or [SELL] in the code where I
 * want you to replace that [BUY] or [SELL] text with YOUR representation of the BUY and SELL side.
 */
/*public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        // First, create an Order and a Tradable variable to use for testing
        Order order1 = null;
        Order order2 = null;
        Tradable tradable1;

        //1a
        try {
            System.out.println("1a) Create and print the content of a valid Order:");
            order1 = new Order("USER1", "GE", PriceFactory.makeLimitPrice("$21.59"), 250, Side.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println(order1); // This should execute
        } catch (Exception e) { // Catch anything you throw.
            // This catch block should be skipped if everything is working as it should.
            System.out.println("Creating of a valid Order resulted in an exception!" + e.getMessage());
            //e.printStackTrace();
        }
        System.out.println();
        //////////
        
        //1b
        try {
            System.out.println("1b) Attempt to create an order using INVALID data (null user name) - should throw an exception:");
            order2 = new Order("", "GE", PriceFactory.makeLimitPrice("$21.59"), 250, Side.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("If this prints then you have accepted invalid data in your Order - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            System.out.println("Properly handled an invalid username! Error message is: " + e.getMessage());
        }
        System.out.println();
        //////////
        
        //1c
        try {
            System.out.println("1c) Attempt to create an order using INVALID data (null Product name) - should throw an exception:");
            order2 = new Order("USER1", "", PriceFactory.makeLimitPrice("$21.59"), 250, Side.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("If this prints then you have accepted invalid data in your Order - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            System.out.println("Properly handled an invalid Product name! Error message is: " + e.getMessage());
        }
        System.out.println();
        //////////

        //2
        System.out.println("2) Refer to the Order using a Tradable reference, and display content:");
        tradable1 = order1;
        System.out.println(tradable1);
        System.out.println();
        //////////
        
        //3
        System.out.println("3) Create and print the content of a TradableDTO (your format may vary, but the data content should be the same):");
        TradableDTO tDTO = new TradableDTO(tradable1.getProduct(), tradable1.getPrice(),
                tradable1.getOriginalVolume(), tradable1.getRemainingVolume(),
                tradable1.getCancelledVolume(), tradable1.getUser(),
                tradable1.getSide(), tradable1.isQuote(), tradable1.getId());
        System.out.println(tDTO);
        System.out.println();
        //////////
        
        //4a
        try {
            System.out.println("4a) Attempt to create an order using INVALID data (Zero volume) - should throw an exception:");
            order1 = new Order("USER1", "GE", PriceFactory.makeLimitPrice("$21.59"), 0, Side.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("If this prints then you have accepted invalid data in your Order - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        //////////
        
        //4b
        try {
            System.out.println("4b) Attempt to create an order using INVALID data (Negative volume) - should throw an exception:");
            order1 = new Order("USER1", "GE", PriceFactory.makeLimitPrice("$21.59"), -9, Side.BUY); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("If this prints then you have accepted invalid data in your Order - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        
        //5a
        System.out.println("5a) Change the cancelled (to 100) and remaining volume (to 150) of the order and display resulting tradable:");
        tradable1.setRemainingVolume(150);
        tradable1.setCancelledVolume(100);
        System.out.println(tradable1);
        System.out.println();
        //////////
        
        //5b
        try {
        System.out.println("5b) Change the cancelled volume to negative qty:");
        tradable1.setRemainingVolume(150);
        tradable1.setCancelledVolume(-200);
        System.out.println(tradable1);
        } catch(Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        
        //5c
        try {
        System.out.println("5c) Change the cancelled volume to one gt original volume:");
        tradable1.setRemainingVolume(150);
        tradable1.setCancelledVolume(500);
        System.out.println(tradable1);
        } catch(Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        
        //5d
        try {
        System.out.println("5d) Change the remaining volume to one gt original volume:");
        tradable1.setRemainingVolume(350);
        tradable1.setCancelledVolume(100);
        System.out.println(tradable1);
        } catch(Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        
        //5e
        try {
        System.out.println("5e) Change the remaining volume to negative:");
        tradable1.setRemainingVolume(-10);
        tradable1.setCancelledVolume(100);
        System.out.println(tradable1);
        } catch(Exception e) { // Catch anything you throw.
            // This block SHOULD execute.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();

        //6 & 7
        Quote quote1;
        try {
            System.out.println("6) Create and print the content of a valid Quote:");
            quote1 = new Quote("USER2", "GE", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), 100);
            System.out.println(quote1);
            System.out.println();

            System.out.println("7) Display the individual Quote Sides of the new Quote object:");
            System.out.println("\t" + quote1.getQuoteSide(Side.BUY)); // Replace "[BUY]" with YOUR BUY representation
            System.out.println("\t" + quote1.getQuoteSide(Side.SELL)); // Replace "[SELL]" with YOUR SELL representation
        } catch (Exception ex) {
            // This catch block should not execute!
            System.out.println("Creating of a valid Quote resulted in an exception!");
            ex.printStackTrace();
        }
        System.out.println();
        //////////

        //8
        try {
            System.out.println("8) Attempt to create a quote using INVALID data (Zero sell volume) - should throw an exception:");
            quote1 = new Quote("USER2", "GE", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), -50);
            System.out.println("If this prints then you have accepted invalid data in your Quote - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            System.out.println("Properly handled an invalid volume! Error message is: " + e.getMessage());
        }
        System.out.println();
        //////////
        
        //9
        Quote quote2;
        try {
            System.out.println("9) Attempt to create a quote using INVALID data (null user name) - should throw an exception:");
            quote2 = new Quote("", "GE", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), 100);
            System.out.println("If this prints then you have accepted invalid data in your Quote - ERROR!");
        } catch (Exception e) { // Catch anything you throw.
            System.out.println("Properly handled an invalid user name! Error message is: " + e.getMessage());
        }
        System.out.println();
    
	    //10
	    try {
	        System.out.println("10) Attempt to create a quote using INVALID data (null Product name) - should throw an exception:");
	        quote2 = new Quote("USER2", "", PriceFactory.makeLimitPrice("$21.56"), 100, PriceFactory.makeLimitPrice("$21.62"), 100);
	        System.out.println("If this prints then you have accepted invalid data in your Quote - ERROR!");
	    } catch (Exception e) { // Catch anything you throw.
	        System.out.println("Properly handled an invalid Product name! Error message is: " + e.getMessage());
	    }
	    System.out.println();
    }//end method main
}//end class Main
*/  //End of Driver - Phase 2
/*
 * Expected Driver Output: Phase 1 Driver
  
1) Create and print the content of a valid Order:
USER1 order: BUY 250 GE at $21.59 (Original Vol: 250, CXL'd Vol: 0), ID: USER1GE$21.5944072698321781

2) Refer to the Order using a Tradable reference, and display content:
USER1 order: BUY 250 GE at $21.59 (Original Vol: 250, CXL'd Vol: 0), ID: USER1GE$21.5944072698321781

3) Create and print the content of a TradableDTO (your format may vary, but the data content should be the same):
Product: GE, Price: $21.59, OriginalVolume: 250, RemainingVolume: 250, CancelledVolume: 0, User: USER1, Side: BUY, IsQuote: false, Id: USER1GE$21.5944072698321781

4) Attempt to create an order using INVALID data (Zero volume) - should throw an exception:
Properly handled an invalid volume! Error message is: Negative or Zero value passed to Order 'setOriginalVolume': 0

5) Change the cancelled and remaining volume of the order and display resulting tradable:
USER1 order: BUY 150 GE at $21.59 (Original Vol: 250, CXL'd Vol: 100), ID: USER1GE$21.5944072698321781

6) Create and print the content of a valid Quote:
USER2 quote: GE $21.56 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703026721] - $21.62 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703047509]

7) Display the individual Quote Sides of the new Quote object:
	$21.56 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703026721]
	$21.62 x 100 (Original Vol: 100, CXL'd Vol: 0) [USER2GE44072703047509]

8) Attempt to create a quote using INVALID data (Zero sell volume) - should throw an exception:
Properly handled an invalid volume! Error message is: Negative value passed to QuoteSide 'setOriginalVolume': -50


 */














//import java.util.ArrayList;
//import org.se450.phase1.Price;
//import org.se450.phase1.PriceFactory;
//import org.se450.phase1.InvalidPriceOperation;
//
//
//public class Main {
//
//    private static ArrayList<Price> testPriceHolder = new ArrayList<>();
//
//    public static void main(String[] args) {
//        makeSomeTestPriceObjects();
//        verifyTestPriceValues();
//        verifyMathematicalOperations();
//        verifyBooleanChecks();
//        verifyComparisons();
//        verifyFlyweight();
//        verifyEquals();
//    }
//
//    private static void makeSomeTestPriceObjects() {
//        System.out.println("Creating some Test Price Objects.");
//        testPriceHolder.add(PriceFactory.makeLimitPrice("10.50"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("$1400.99"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("$-51.52"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice(".49"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("-0.89"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("12"));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("90."));
//        testPriceHolder.add(PriceFactory.makeLimitPrice("14.5"));
//        testPriceHolder.add(PriceFactory.makeMarketPrice());
//    }
//    
//    private static void verifyTestPriceValues() {
//        System.out.println("Verifying the Values in Your Test Price Objects:");
//        String format = "%-9s --> %9s : %s%n";
//        System.out.format(format, "$10.50", testPriceHolder.get(0), testPriceHolder.get(0).toString().equals("$10.50") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$1,400.99", testPriceHolder.get(1), testPriceHolder.get(1).toString().equals("$1,400.99") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$-51.52", testPriceHolder.get(2), testPriceHolder.get(2).toString().equals("$-51.52") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$0.49", testPriceHolder.get(3), testPriceHolder.get(3).toString().equals("$0.49") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$-0.89", testPriceHolder.get(4), testPriceHolder.get(4).toString().equals("$-0.89") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$12.00", testPriceHolder.get(5), testPriceHolder.get(5).toString().equals("$12.00") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$90.00", testPriceHolder.get(6), testPriceHolder.get(6).toString().equals("$90.00") ? "CORRECT" : "ERROR");
//        System.out.format(format, "$14.50", testPriceHolder.get(7), testPriceHolder.get(7).toString().equals("$14.50") ? "CORRECT" : "ERROR");
//        System.out.format(format, "MKT", testPriceHolder.get(8), testPriceHolder.get(8).toString().equals("MKT") ? "CORRECT" : "ERROR");
//        System.out.println();
//    }
//
//    private static void verifyMathematicalOperations() {
//        System.out.println("Verifying the Functionality of your Mathematical Operations:");
//        String format = "%-9s %c %9s = %9s : %s%n";
//        try {
//            Price results = testPriceHolder.get(0).add(testPriceHolder.get(1));
//            System.out.format(format, testPriceHolder.get(0), '+', testPriceHolder.get(1), results, results.toString().equals("$1,411.49") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            Price results = testPriceHolder.get(1).subtract(testPriceHolder.get(1));
//            System.out.format(format, testPriceHolder.get(1), '-', testPriceHolder.get(1), results, results.toString().equals("$0.00") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            Price results = testPriceHolder.get(2).add(testPriceHolder.get(3));
//            System.out.format(format, testPriceHolder.get(2), '+', testPriceHolder.get(3), results, results.toString().equals("$-51.03") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            Price results = testPriceHolder.get(3).multiply(4);
//            System.out.format(format, testPriceHolder.get(3), '*', 4, results, results.toString().equals("$1.96") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            Price results = testPriceHolder.get(4).subtract(testPriceHolder.get(5));
//            System.out.format(format, testPriceHolder.get(4), '-', testPriceHolder.get(5), results, results.toString().equals("$-12.89") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            Price results = testPriceHolder.get(5).add(testPriceHolder.get(6));
//            System.out.format(format, testPriceHolder.get(5), '+', testPriceHolder.get(6), results, results.toString().equals("$102.00") ? "CORRECT" : "ERROR");
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("FAILED: " + ex.getMessage());
//        }
//        try {
//            testPriceHolder.get(8).add(testPriceHolder.get(0));
//            System.out.println("ERROR: Adding a LIMIT price to a MARKET Price succeeded: " + testPriceHolder.get(8) + " + " + testPriceHolder.get(0));
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " + " + testPriceHolder.get(0));
//        }
//        try {
//            testPriceHolder.get(8).subtract(testPriceHolder.get(0));
//            System.out.println("ERROR: Subtracting a LIMIT price from a MARKET Price succeeded: " + testPriceHolder.get(8) + " - " + testPriceHolder.get(0));
//
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " - " + testPriceHolder.get(0));
//        }
//        try {
//            testPriceHolder.get(8).multiply(10);
//            System.out.println("ERROR: Multiplying a MARKET price succeeded: " + testPriceHolder.get(8) + " + 10");
//
//        } catch (InvalidPriceOperation ex) {
//            System.out.println("CORRECT: " + ex.getMessage() + ": " + testPriceHolder.get(8) + " * 10");
//        }
//        System.out.println();
//    }
//
//    private static void verifyBooleanChecks() {
//        System.out.println("Verifying the Functionality of your Boolean Checks:");
//        System.out.println("Value      | Is Negative | Is Market");
//        System.out.println("------------------------------------");
//        String format = "%-9s  | %-12s| %-12s%n";
//        System.out.format(format, testPriceHolder.get(0), testPriceHolder.get(0).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(0).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(1), testPriceHolder.get(1).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(1).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(2), testPriceHolder.get(2).isNegative() ? "CORRECT" : "ERROR", testPriceHolder.get(2).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(3), testPriceHolder.get(3).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(3).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(4), testPriceHolder.get(4).isNegative() ? "CORRECT" : "ERROR", testPriceHolder.get(4).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(5), testPriceHolder.get(5).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(5).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(6), testPriceHolder.get(6).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(6).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(7), testPriceHolder.get(7).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(7).isMarket() ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(8), testPriceHolder.get(8).isNegative() ? "ERROR" : "CORRECT", testPriceHolder.get(8).isMarket() ? "CORRECT" : "ERROR");
//        System.out.println();
//    }
//
//    private static void verifyComparisons() {
//        System.out.println("Verifying the Functionality of your Boolean Comparisons:");
//        Price testPrice = testPriceHolder.get(7);
//
//        String format = "%-10s | %-15s | %-12s | %-12s | %-9s%n";
//        System.out.println("Comparison\nto " + testPrice + "  | greaterOrEqual  | greaterThan  | lessOrEqual  | lessThan");
//        System.out.println("---------------------------------------------------------------------");
//        System.out.format(format, testPriceHolder.get(0),
//                testPriceHolder.get(0).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(0).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(0).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(0).lessThan(testPrice) ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(1),
//                testPriceHolder.get(1).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(1).greaterThan(testPrice) ? "CORRECT" : "ERROR",
//                testPriceHolder.get(1).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(1).lessThan(testPrice) ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(2),
//                testPriceHolder.get(2).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(2).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(2).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(2).lessThan(testPrice) ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(3),
//                testPriceHolder.get(3).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(3).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(3).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(3).lessThan(testPrice) ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(4),
//                testPriceHolder.get(4).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(4).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(4).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(4).lessThan(testPrice) ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(5),
//                testPriceHolder.get(5).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(5).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(5).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(5).lessThan(testPrice) ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(6),
//                testPriceHolder.get(6).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(6).greaterThan(testPrice) ? "CORRECT" : "ERROR",
//                testPriceHolder.get(6).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(6).lessThan(testPrice) ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(7),
//                testPriceHolder.get(7).greaterOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(7).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(7).lessOrEqual(testPrice) ? "CORRECT" : "ERROR", testPriceHolder.get(7).lessThan(testPrice) ? "ERROR" : "CORRECT");
//        System.out.format(format, testPriceHolder.get(8),
//                testPriceHolder.get(8).greaterOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(8).greaterThan(testPrice) ? "ERROR" : "CORRECT",
//                testPriceHolder.get(8).lessOrEqual(testPrice) ? "ERROR" : "CORRECT", testPriceHolder.get(8).lessThan(testPrice) ? "ERROR" : "CORRECT");
//        System.out.println();
//
//
//    }
//
//    private static void verifyFlyweight() {
//        System.out.println("Verifying your Flyweight Implementation:");
//        String format = "Price %-9s is same object as new %9s: %s%n";
//        Price p1 = PriceFactory.makeLimitPrice("10.50");
//        System.out.format(format, testPriceHolder.get(0), p1, testPriceHolder.get(0) == p1 ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(1), p1, testPriceHolder.get(1) == p1 ? "ERROR" : "CORRECT");
//        
//        Price p2 = PriceFactory.makeLimitPrice("10.50");
//        System.out.format(format, p1, p2, p1 == p2 ? "CORRECT" : "ERROR");
//
//        p1 = PriceFactory.makeMarketPrice();
//        System.out.format(format, testPriceHolder.get(8), p1, testPriceHolder.get(8) == p1 ? "CORRECT" : "ERROR");
//        System.out.format(format, testPriceHolder.get(1), p1, testPriceHolder.get(1) == p1 ? "ERROR" : "CORRECT");
//        
//        
//        
//        System.out.println();
//    }
//    
//    private static void verifyEquals()
//    {
//    	System.out.println("Verifying equals Implementation:");
//    	Price p1 = PriceFactory.makeLimitPrice("8.5");
//    	Price p2 = PriceFactory.makeLimitPrice("8.5");
//    	System.out.println(p1 + " equals " + p2 + "? ==> " + p1.equals(p2));
//    	
//    	Price p3 = PriceFactory.makeLimitPrice("8.57");
//    	Price p4 = PriceFactory.makeLimitPrice("9");
//    	System.out.println(p3 + " equals " + p4 + "? ==> " + p3.equals(p4));
//    	
//    	
//    	Price p5 = PriceFactory.makeLimitPrice("0");
//    	Price p6 = PriceFactory.makeMarketPrice();
//    	System.out.println(p5 + " equals " + p6 + "? ==> " + p5.equals(p6));
//    	
//    	Price p7 = PriceFactory.makeLimitPrice("0");
//    	Price p8 = PriceFactory.makeLimitPrice("0");
//    	System.out.println(p7 + " equals " + p8 + "? ==> " + p7.equals(p8));
//    	
//    	Price p9 = PriceFactory.makeMarketPrice();
//    	Price p10 = PriceFactory.makeMarketPrice();
//    	System.out.println(p9 + " equals " + p10 + "? ==> " + p9.equals(p10));
//    }
//}  End of Driver - Phase 1











