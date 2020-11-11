package viewmodel;

import model.BookingModel;

public class ViewModelFactory
{
  private BasicInformationViewModel basicInformationViewModel;
  private BookingsViewModel bookingsViewModel;
  private CancelViewModel cancelViewModel;
  private EndViewModel endViewModel;
  private LoginViewModel loginViewModel;
  private OrderViewModel orderViewModel;
  private PasswordViewModel passwordViewModel;
  private ProfileViewModel profileViewModel;
  private RegisterViewModel registerViewModel;
  private ReserveViewModel reserveViewModel;
  private RoomViewModel roomViewModel;

  public ViewModelFactory(BookingModel model)
  {
    basicInformationViewModel = new BasicInformationViewModel(model);
    bookingsViewModel = new BookingsViewModel(model);
    cancelViewModel = new CancelViewModel(model);
    endViewModel = new EndViewModel(model);
    loginViewModel = new LoginViewModel(model);
    orderViewModel = new OrderViewModel(model);
    passwordViewModel = new PasswordViewModel(model);
    profileViewModel = new ProfileViewModel(model);
    registerViewModel = new RegisterViewModel(model);
    reserveViewModel = new ReserveViewModel(model);
    roomViewModel = new RoomViewModel(model);
  }

  public BasicInformationViewModel getBasicInformationViewModel()
  {
    return basicInformationViewModel;
  }

  public BookingsViewModel getBookingsViewModel()
  {
    return bookingsViewModel;
  }

  public CancelViewModel getCancelViewModel()
  {
    return cancelViewModel;
  }

  public EndViewModel getEndViewModel()
  {
    return endViewModel;
  }

  public LoginViewModel getLoginViewModel()
  {
    return loginViewModel;
  }

  public OrderViewModel getOrderViewModel()
  {
    return orderViewModel;
  }

  public PasswordViewModel getPasswordViewModel()
  {
    return passwordViewModel;
  }

  public ProfileViewModel getProfileViewModel()
  {
    return profileViewModel;
  }

  public RegisterViewModel getRegisterViewModel()
  {
    return registerViewModel;
  }

  public ReserveViewModel getReserveViewModel()
  {
    return reserveViewModel;
  }

  public RoomViewModel getRoomViewModel()
  {
    return roomViewModel;
  }

  public void init()
  {
    reserveViewModel.init();
  }
}
