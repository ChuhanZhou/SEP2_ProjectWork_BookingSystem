package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import viewmodel.*;

import java.io.IOException;

public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage primaryStage;
  private Scene currentScene;
  private HotelUpdateViewController hotelUpdateViewController;
  private HotelViewController hotelViewController;
  private OrderViewController orderViewController;
  private RoomTypeUpdateViewController roomTypeUpdateViewController;
  private RoomTypeViewController roomTypeViewController;
  private RoomViewController roomViewController;
  private SearchViewController searchViewController;
  private ServiceViewController serviceViewController;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    currentScene = new Scene(new Region());
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    openView("HotelView");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
      case "HotelView":
        root = loadHotelView("HotelView.fxml");
        primaryStage.setTitle("HotelView");
        break;
      case "HotelUpdateView":
        root = loadHotelUpdateView("HotelUpdateView.fxml");
        primaryStage.setTitle("HotelUpdateView");
        break;
      case "OrderView":
        root = loadOrderView("OrderView.fxml");
        primaryStage.setTitle("OrderView");
        break;
      case "RoomTypeUpdateView":
        root = loadRoomTypeUpdateView("RoomTypeUpdateView.fxml");
        primaryStage.setTitle("RoomTypeUpdateView");
        break;
      case "RoomTypeView":
        root = loadRoomTypeView("RoomTypeView.fxml");
        primaryStage.setTitle("RoomTypeView");
        break;
      case "RoomView":
        root = loadRoomView("RoomView.fxml");
        primaryStage.setTitle("RoomView");
        break;
      case "SearchView":
        root = loadSearchView("SearchView.fxml");
        primaryStage.setTitle("SearchView");
        break;
      case "ServiceView":
        root = loadServiceView("ServiceView.fxml");
        primaryStage.setTitle("ServiceView");
        break;
    }
    currentScene.setRoot(root);
    primaryStage.setScene(currentScene);
    primaryStage.setWidth(root.getPrefWidth());
    primaryStage.setHeight(root.getPrefHeight());
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public ViewModelFactory getViewModelFactory()
  {
    return viewModelFactory;
  }

  private Region loadHotelView(String fxmlFile)
  {
    Region root;
    if (hotelViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        hotelViewController = loader.getController();
        hotelViewController.init(this,viewModelFactory.getHotelViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      hotelViewController.reset();
    }
    return hotelViewController.getRoot();
  }

  private Region loadHotelUpdateView(String fxmlFile)
  {
    Region root;
    if (hotelUpdateViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        hotelUpdateViewController = loader.getController();
        hotelUpdateViewController.init(this,viewModelFactory.getHotelUpdateViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      hotelUpdateViewController.reset();
    }
    return hotelUpdateViewController.getRoot();
  }

  private Region loadOrderView(String fxmlFile)
  {
    Region root;
    if (orderViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        orderViewController = loader.getController();
        orderViewController.init(this,viewModelFactory.getOrderViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      orderViewController.reset();
    }
    return orderViewController.getRoot();
  }

  private Region loadRoomTypeUpdateView(String fxmlFile)
  {
    Region root;
    if (roomTypeUpdateViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        roomTypeUpdateViewController = loader.getController();
        roomTypeUpdateViewController.init(this,viewModelFactory.getRoomTypeUpdateViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      roomTypeUpdateViewController.reset();
    }
    return roomTypeUpdateViewController.getRoot();
  }

  private Region loadRoomTypeView(String fxmlFile)
  {
    Region root;
    if (roomTypeViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        roomTypeViewController = loader.getController();
        roomTypeViewController.init(this,viewModelFactory.getRoomTypeViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      roomTypeViewController.reset();
    }
    return roomTypeViewController.getRoot();
  }

  private Region loadRoomView(String fxmlFile)
  {
    Region root;
    if (roomViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        roomViewController = loader.getController();
        roomViewController.init(this,viewModelFactory.getRoomViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      roomViewController.reset();
    }
    return roomViewController.getRoot();
  }

  private Region loadSearchView(String fxmlFile)
  {
    Region root;
    if (searchViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        searchViewController = loader.getController();
        searchViewController.init(this,viewModelFactory.getSearchViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      searchViewController.reset();
    }
    return searchViewController.getRoot();
  }

  private Region loadServiceView(String fxmlFile)
  {
    Region root;
    if (serviceViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        serviceViewController = loader.getController();
        serviceViewController.init(this,viewModelFactory.getServiceViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      serviceViewController.reset();
    }
    return serviceViewController.getRoot();
  }
}
