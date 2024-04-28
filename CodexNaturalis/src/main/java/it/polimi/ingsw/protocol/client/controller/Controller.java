package it.polimi.ingsw.protocol.client.controller;

public abstract class Controller {
    //Controller is an abstract class so cannot be instantiated, the View will instantiate a ControllerSocket
    //or ControllerRMI object depending on which connection the user chooses
    //the controller methods will allow the client to handle the user inputs and send them to the server
    //the methods of the Controller will then be different as their implementation will change in ControllerSocket and ControllerRMI classes

    String ServerIP;
    String ServerPort;



    /**
     * this method will allow the Client to connect to the server, it is an abstract method as its implementation
     * will change if the Client decide to connect with Socket or RMI and so instantiate a ControllerSocket or ControllerRMI object
     * @param IP of the server
     * @param port of the server
     */
    public abstract void connectToServer(String IP, String port);

    /**
     *this method will allow the client to choose his username
     * @param name
     */
    public abstract void chooseNickname(String name);

    /**
     * this method will allow the client to choose his color
     * @param color
     */
    public abstract void chooseColor(String color);

    /**
     * this method will allow the client to choose his secret objective
     * @param pick
     * @return true if the objective is picked correctly
     */
    public abstract boolean pickedObjective(int pick);

    /**
     *
     * @param side
     * @return true if the card is placed correctly
     */
    public abstract boolean placeStarter(int side);

    /**
     * this method will allow the client to communicate to the server which card he wants to play, in which position and on which side
     * @param card
     * @param side
     * @param x
     * @param y
     * @return true if the card is placed correctly
     */
    public abstract boolean placeCard(int card, int side, int x, int y);

    /**
     *
     * @param card
     * @return true if the card is picked correctly
     */
    public abstract boolean pickCard(int card);
}
