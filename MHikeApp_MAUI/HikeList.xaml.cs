using System;
using Microsoft.Maui.Controls;

namespace MauiApp2;

public partial class HikeList : ContentPage
{
    App thisApp = Microsoft.Maui.Controls.Application.Current as App;
    private MySQLiteDatabase myDB = new MySQLiteDatabase();

    public HikeList()
    {
        InitializeComponent();
        collectionView.ItemsSource = thisApp.HikeList;
    }

    // Called by Delete button in item template (Clicked="OnDeleteClicked")
    private async void OnDeleteClicked(object sender, EventArgs e)
    {
        var button = sender as Button;
        if (button?.CommandParameter is not Hike hike)
            return;

        bool confirm = await DisplayAlertAsync("Confirm", $"Delete {hike.Name}?", "Yes", "No");
        if (!confirm)
            return;

        // Remove from database and app collection so UI updates
        myDB.DeleteItem(hike);
        thisApp?.HikeList?.Remove(hike);
    }

    private async void OnEditClicked(object sender, EventArgs e)
    {
        var button = sender as Button;
        if (button?.CommandParameter is not Hike hike) return;
        await Navigation.PushModalAsync(new HikeEditPage(hike, myDB));
    }
}