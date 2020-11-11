package view;

import javafx.fxml.FXMLLoader;
import viewmodel.*;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage primaryStage;
  private Scene currentScene;
  private BasicInformationViewController basicInformationViewController;
  private BookingsViewController bookingsViewController;
  private CancelViewController cancelViewController;
  private EndViewController endViewController;
  private LoginViewController loginViewController;
  private OrderViewController orderViewController;
  private PasswordViewController passwordViewController;
  private ProfileViewController profileViewController;
  private RegisterViewController registerViewController;
  private ReserveViewController reserveViewController;
  private RoomViewController roomViewController;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    currentScene = new Scene(new Region());
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    openView("LoginView");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
      case "LoginView":
        root = loadLoginView("LoginView.fxml");
        primaryStage.setTitle("LoginView");
        break;
      case "BasicInformationView":
        root = loadBasicInformationView("BasicInformationView.fxml");
        primaryStage.setTitle("BasicInformationView");
        break;
      case "BookingsView":
        root = loadBookingsView("BookingsView.fxml");
        primaryStage.setTitle("BookingsView");
        break;
      case "CancelView":
        root = loadCancelView("CancelView.fxml");
        primaryStage.setTitle("CancelView");
        break;
      case "EndView":
        root = loadEndView("EndView.fxml");
        primaryStage.setTitle("EndView");
        break;
      case "OrderView":
        root = loadOrderView("OrderView.fxml");
        primaryStage.setTitle("OrderView");
        break;
      case "PasswordView":
        root = loadPasswordView("PasswordView.fxml");
        primaryStage.setTitle("PasswordView");
        break;
      case "ProfileView":
        root = loadProfileView("ProfileView.fxml");
        primaryStage.setTitle("ProfileView");
        break;
      case "RegisterView":
        root = loadRegisterView("RegisterView.fxml");
        primaryStage.setTitle("RegisterView");
        break;
      case "ReserveView":
        root = loadReserveView("ReserveView.fxml");
        primaryStage.setTitle("ReserveView");
        break;
      case "RoomView":
        root = loadRoomView("RoomView.fxml");
        primaryStage.setTitle("RoomView");
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

  private Region loadLoginView(String fxmlFile)
  {
    Region root;
    if (loginViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        loginViewController = loader.getController();
        loginViewController.init(this,viewModelFactory.getLoginViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      loginViewController.reset();
    }
    return loginViewController.getRoot();
  }

  private Region loadBasicInformationView(String fxmlFile)
  {
    Region root;
    if (basicInformationViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        basicInformationViewController = loader.getController();
        basicInformationViewController.init(this,viewModelFactory.getBasicInformationViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      basicInformationViewController.reset();
    }
    return basicInformationViewController.getRoot();
  }

  private Region loadBookingsView(String fxmlFile)
  {
    Region root;
    if (bookingsViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        bookingsViewController = loader.getController();
        bookingsViewController.init(this,viewModelFactory.getBookingsViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      bookingsViewController.reset();
    }
    return bookingsViewController.getRoot();
  }

  private Region loadCancelView(String fxmlFile)
  {
    Region root;
    if (cancelViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        cancelViewController = loader.getController();
        cancelViewController.init(this,viewModelFactory.getCancelViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return cancelViewController.getRoot();
  }

  private Region loadEndView(String fxmlFile)
  {
    Region root;
    if (endViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        endViewController = loader.getController();
        endViewController.init(this,viewModelFactory.getEndViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      endViewController.reset();
    }
    return endViewController.getRoot();
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

  private Region loadPasswordView(String fxmlFile)
  {
    Region root;
    if (passwordViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        passwordViewController = loader.getController();
        passwordViewController.init(this,viewModelFactory.getPasswordViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      passwordViewController.reset();
    }
    return passwordViewController.getRoot();
  }

  private Region loadProfileView(String fxmlFile)
  {
    Region root;
    if (profileViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        profileViewController = loader.getController();
        profileViewController.init(this,viewModelFactory.getProfileViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      profileViewController.reset();
    }
    return profileViewController.getRoot();
  }

  private Region loadRegisterView(String fxmlFile)
  {
    Region root;
    if (registerViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        registerViewController = loader.getController();
        registerViewController.init(this,viewModelFactory.getRegisterViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      registerViewController.reset();
    }
    return registerViewController.getRoot();
  }

  private Region loadReserveView(String fxmlFile)
  {
    Region root;
    if (reserveViewController==null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        root = loader.load();
        reserveViewController = loader.getController();
        reserveViewController.init(this,viewModelFactory.getReserveViewModel(),root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      reserveViewController.reset();
    }
    return reserveViewController.getRoot();
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
}
