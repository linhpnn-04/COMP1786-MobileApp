using System.Threading.Tasks;
using System.Diagnostics;

namespace MauiApp2;

public partial class MainPage : ContentPage
{
    int count = 0;
    App thisApp = Microsoft.Maui.Controls.Application.Current as App;
    private MySQLiteDatabase myDB;
    private Entry _txtName;
    private Entry _txtLocation;
    private DatePicker _dtDate;
    private RadioButton _rbParkingYes;
    private Entry _txtLength;
    private Picker _pkrDifficulty;
    private Editor _txtDescription;

    public MainPage()
    {
        InitializeComponent();

        // Resolve controls by name at runtime — works even if XAML code-gen isn't available.
        _txtName = this.FindByName<Entry>("txtName");
        _txtLocation = this.FindByName<Entry>("txtLocation");
        _dtDate = this.FindByName<DatePicker>("dtDate");
        _rbParkingYes = this.FindByName<RadioButton>("rbParkingYes");
        _txtLength = this.FindByName<Entry>("txtLength");
        _pkrDifficulty = this.FindByName<Picker>("pkrDifficulty");
        _txtDescription = this.FindByName<Editor>("txtDescription");

        // Ensure HikeList exists
        thisApp.HikeList = thisApp.HikeList ?? new System.Collections.ObjectModel.ObservableCollection<Hike>();
        myDB = new MySQLiteDatabase();
        // Defensive: ensure Clicked handler is wired at runtime (avoids XAML mismatch)
        var btn = this.FindByName<Button>("btnSave");
        if (btn != null)
            btn.Clicked -= OnSaveClicked; // avoid double-subscribe in hot reload
        btn.Clicked += OnSaveClicked;
    }

    // Must be async void for XAML Clicked event wiring
    private async void OnSaveClicked(object sender, EventArgs e)
    {
        try
        {

            // 1. Read values from controls
            string name = _txtName?.Text;
            string location = _txtLocation?.Text;
            DateTime date = _dtDate?.Date ?? DateTime.Today;
            bool isParking = _rbParkingYes?.IsChecked ?? false;
            string length = _txtLength?.Text;
            string difficulty = _pkrDifficulty?.SelectedItem as string;
            string description = _txtDescription?.Text;

            // 2. Validation
            if (string.IsNullOrEmpty(name))
            {
                await DisplayAlert("Error", "Name is required!", "OK");
                return;
            }
            if (string.IsNullOrEmpty(location))
            {
                await DisplayAlert("Error", "Location is required!", "OK");
                return;
            }
            if (string.IsNullOrEmpty(length))
            {
                await DisplayAlert("Error", "Length is required!", "OK");
                return;
            }
            if (string.IsNullOrEmpty(difficulty))
            {
                await DisplayAlert("Error", "Please select difficulty level!", "OK");
                return;
            }

            // 3. Confirmation summary
            string parkingText = isParking ? "Yes" : "No";
            string summary = $"Name: {name}\nLocation: {location}\nDate: {date:dd/MM/yyyy}\nParking: {parkingText}\nLength: {length}\nDifficulty: {difficulty}\nDescription: {description}";

            await DisplayAlert("Hike Details", summary, "OK");

            bool response = await DisplayAlert("Confirm Save", "Do you want to save this hike?", "Yes", "No");
            if (response)
            {
                if (thisApp == null)
                {
                    Debug.WriteLine("Application.Current is null.");
                    await DisplayAlert("Error", "Application instance not available.", "OK");
                    return;
                }

                if (thisApp.HikeList == null)
                    thisApp.HikeList = new System.Collections.ObjectModel.ObservableCollection<Hike>();

                // 4. Save
                Hike newHike = new Hike(
                    thisApp.HikeList.Count + 1,
                    name,
                    location,
                    date,
                    isParking,
                    length,
                    difficulty,
                    description
                );
                thisApp.HikeList.Add(newHike);
                myDB.insertHike(newHike);

                // 5. Clear controls
                _txtName.Text = string.Empty;
                _txtLocation.Text = string.Empty;
                _dtDate.Date = DateTime.Today;
                _rbParkingYes.IsChecked = false;
                _txtLength.Text = string.Empty;
                _pkrDifficulty.SelectedIndex = -1;
                _txtDescription.Text = string.Empty;

                await DisplayAlert("Success", "Hike saved successfully!", "OK");
            }
        }
        catch (Exception ex)
        {
            Debug.WriteLine(ex);
            await DisplayAlert("Unhandled error", ex.Message, "OK");
        }


    }

    // Hàm này chạy mỗi khi màn hình hiện lên
    protected override async void OnAppearing()
    {
        base.OnAppearing();


        // 👇 THÊM ĐOẠN NÀY ĐỂ LẤY FILE 👇
        // 1. Lấy đường dẫn file DB (sửa tên file cho đúng với code bạn)
        string dbName = "MySQLiteDatabase.db3";
        string dbPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), dbName);

        // 2. Kiểm tra xem file có tồn tại không
        if (File.Exists(dbPath))
        {
            // Hiện hộp thoại hỏi muốn lưu file đi đâu
            await Share.RequestAsync(new ShareFileRequest
            {
                Title = "Xuất file Database",
                File = new ShareFile(dbPath)
            });
        }
    }
    private void btnView_Clicked(object sender, EventArgs e)
    {
        Navigation.PushModalAsync(new HikeList(), true);
    }

    private void btnLoad_Clicked(object sender, EventArgs e)
    {
        thisApp.HikeList.Clear();
        var hikesFromDb = myDB.loadHike();
        foreach (var hike in hikesFromDb)
        {
            thisApp.HikeList.Add(hike);
        }
    }
}