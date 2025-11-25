using System;
using Microsoft.Maui.Controls;

namespace MauiApp2;

public partial class HikeEditPage : ContentPage
{
    private readonly Hike _hike;
    private readonly MySQLiteDatabase _db;

    public HikeEditPage(Hike hike, MySQLiteDatabase db)
    {
        InitializeComponent();
        _hike = hike ?? throw new ArgumentNullException(nameof(hike));
        _db = db ?? throw new ArgumentNullException(nameof(db));

        // check control wiring (if any control is null, likely XAML name mismatch)
        if (txtName == null || dtDate == null || pkrDifficulty == null)
            throw new InvalidOperationException("One or more XAML controls are null. Check x:Name and x:Class in XAML.");

        // populate controls with guards
        txtName.Text = _hike.Name;
        txtLocation.Text = _hike.Location;
        dtDate.Date = DateTime.Today;        // Guard DatePicker range (avoid DateTime.MinValue problems)
        var min = dtDate.MinimumDate;
        var max = dtDate.MaximumDate;
        var dateToSet = (_hike.Date < min || _hike.Date > max) ? DateTime.Today : _hike.Date;
        dtDate.Date = dateToSet;

        rbParkingYes.IsChecked = false;
        txtLength.Text = string.Empty;
        pkrDifficulty.SelectedIndex = -1;
        txtDescription.Text = string.Empty;
        if (!string.IsNullOrEmpty(_hike.Difficulty) && pkrDifficulty.ItemsSource != null)
        {
            // select the matching existing item (string comparison)
            foreach (var item in pkrDifficulty.ItemsSource)
            {
                if (item?.ToString() == _hike.Difficulty)
                {
                    pkrDifficulty.SelectedItem = item;
                    break;
                }
            }
        }
    }

    private async void OnSaveClicked(object sender, EventArgs e)
    {
        if (string.IsNullOrWhiteSpace(txtName.Text))
        {
            await DisplayAlert("Validation", "Name is required.", "OK");
            return;
        }

        _hike.Name = txtName.Text.Trim();
        _hike.Location = txtLocation.Text?.Trim() ?? string.Empty;
        _hike.Date = dtDate.Date ?? DateTime.Today;
        _hike.IsParking = rbParkingYes.IsChecked;
        _hike.Length = txtLength.Text?.Trim() ?? string.Empty;
        _hike.Difficulty = pkrDifficulty.SelectedItem as string ?? string.Empty;
        _hike.Description = txtDescription.Text ?? string.Empty;

        _db.UpdateHike(_hike);

        // update app collection so UI refreshes (replace item if Hike doesn't implement INotifyPropertyChanged)
        var app = Application.Current as App;
        var list = app?.HikeList;
        if (list != null)
        {
            int idx = list.IndexOf(_hike);
            if (idx >= 0) list[idx] = _hike;
        }

        await Navigation.PopModalAsync();
    }

    private async void OnCancelClicked(object sender, EventArgs e)
    {
        await Navigation.PopModalAsync();
    }
}