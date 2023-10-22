import SwiftUI
import Combine
import FantasyPremierLeagueKit



extension GameFixture: Identifiable { }

struct FixtureListView: View {
    @ObservedObject var viewModel: FantasyPremierLeagueViewModel

    
    var body: some View {
        VStack {
            NavigationView {
                VStack(spacing: 0) {
                    HStack {
                        Button(action: {
                            if (viewModel.gameWeek > 1) { viewModel.gameWeek = viewModel.gameWeek - 1 }
                        }) {
                          Image(systemName: "arrow.left")
                        }
                        Text("Gameweek \(viewModel.gameWeek)")
                        Button(action: {
                            if (viewModel.gameWeek < 38) { viewModel.gameWeek = viewModel.gameWeek + 1 }
                        }) {
                          Image(systemName: "arrow.right")
                        }
                    }
                    List(viewModel.gameWeekFixtures[viewModel.gameWeek] ?? []) { fixture in
                        NavigationLink(destination: FixtureDetailView(fixture: fixture, onSubmitPredict: { prediction in viewModel.onSubmitPredict(prediction: prediction) })) {
                            FixtureView(fixture: fixture)
                        }
                    }
                    .listStyle(.plain)
                    .navigationBarTitle(Text("Fixtures"))
                    .onAppear {
                        UITableView.appearance().separatorStyle = .none
                    }
                    .task {
                        await viewModel.getGameWeekFixtures()
                    }
                }
            }
            .onAppear {
                UITableView.appearance().separatorStyle = .none
            }
        }
    }
}


struct FixtureView: View {
    let fixture: GameFixture
    
    var body: some View {
        VStack {
            PredictView(fixture: fixture)
            HStack {
                ClubInFixtureView(teamName: fixture.homeTeam, teamPhotoUrl: fixture.homeTeamPhotoUrl)
                Spacer()
                Text(fixture.homeScore).font(.system(size: 20))
                Spacer()
                Text("vs").font(.system(size: 22))
                Spacer()
                Text(fixture.awayScore).font(.system(size: 20))
                Spacer()
                ClubInFixtureView(teamName: fixture.awayTeam, teamPhotoUrl: fixture.awayTeamPhotoUrl)
            }
            let formattedTime = String(format: "%02d:%02d", fixture.localKickoffTime.hour, fixture.localKickoffTime.minute)
            Text(fixture.localKickoffTime.date.description()).font(.system(size: 14)).padding(.top, 10)
            Text(formattedTime).font(.system(size: 14))
        }.padding(8)
    }
}

struct PredictView: View {
    let fixture: GameFixture

    var body: some View {
            if fixture.isLive {
                VStack {
                    Text(fixture.isPredicted ? "Prediction submitted! Watch the game live!" : "Live!")
                        .font(.system(size: 14))
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding(8)
                        .background(LinearGradient(gradient: Gradient(colors: [Color(red: 0.77, green: 0.66, blue: 1.00), Color(red: 0.49, green: 0.26, blue: 0.64)]), startPoint: .top, endPoint: .bottom))
                        .clipShape(RoundedRectangle(cornerRadius: 10))
                }
                .frame(maxWidth: .infinity)
                .padding(8)
            } else if fixture.isNotStartedYet {
                VStack {
                    Text(fixture.isPredicted ? "Prediction submitted!" : "Join the Betting Action!")
                        .font(.system(size: 14))
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding(8)
                        .background(LinearGradient(gradient: Gradient(colors: [Color(red: 0.77, green: 0.66, blue: 1.00), Color(red: 0.49, green: 0.26, blue: 0.64)]), startPoint: .top, endPoint: .bottom))
                        .clipShape(RoundedRectangle(cornerRadius: 10))
                }
                .frame(maxWidth: .infinity)
                .padding(8)
            }
        }
}

struct ClubInFixtureView: View {
    let teamName: String
    let teamPhotoUrl: String
    
    var body: some View {
        VStack {
            AsyncImage(url: URL(string: teamPhotoUrl)) { image in
                 image.resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 50, height: 50)
            } placeholder: {
                ProgressView()
            }
            Text(teamName).font(.system(size: 14)).lineLimit(1)
        }.frame(minWidth: 80)
    }
}
