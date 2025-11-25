using Microsoft.Extensions.DependencyInjection;
using System.Collections.ObjectModel;

namespace MauiApp2
{
    public partial class App : Application
    {
        public ObservableCollection<Hike> HikeList;
        public App()
        {
            InitializeComponent();
            MainPage = new AppShell();

        }

    }
    }
