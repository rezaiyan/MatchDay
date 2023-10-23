import SwiftUI
import FantasyPremierLeagueKit



struct ContentView: View {
    @StateObject var viewModel = FantasyPremierLeagueViewModel(repository: FantasyPremierLeagueRepository())

    @State private var selectedTab = 1

       var body: some View {
           TabView(selection: $selectedTab) {
               PlayerListView(viewModel: viewModel)
                   .tabItem {
                       Label("Players", systemImage: "person")
                   }
                   .tag(0) // First tab
               FixtureListView(viewModel: viewModel)
                   .tabItem {
                       Label("Fixtures", systemImage: "clock")
                   }
                   .tag(1) // Second tab
               LeagueListView(viewModel: viewModel)
                   .tabItem {
                       Label("League", systemImage: "list.number")
                   }
                   .tag(2) // Third tab
           }
       }
}











