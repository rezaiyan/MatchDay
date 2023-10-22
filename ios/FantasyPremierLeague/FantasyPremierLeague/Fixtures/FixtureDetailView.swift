import SwiftUI
import FantasyPremierLeagueKit

struct FixtureDetailView: View {
    var fixture: GameFixture
    var onSubmitPredict: (Prediction) -> Void
    
    var body: some View {
        
        VStack {
            HStack {
                ClubInFixtureView(teamName: fixture.homeTeam, teamPhotoUrl: fixture.homeTeamPhotoUrl)
                Spacer()
                Text("(\(fixture.homeTeamScore ?? 0))").font(.system(size: 22))
                Spacer()
                Text("vs").font(.system(size: 22))
                Spacer()
                Text("(\(fixture.awayTeamScore ?? 0))").font(.system(size: 22))
                Spacer()
                ClubInFixtureView(teamName: fixture.awayTeam, teamPhotoUrl: fixture.awayTeamPhotoUrl)
            }
            
            if fixture.isNotStartedYet || fixture.isPredicted {
                PredictionView(gameFixture: fixture, onSubmitPredict: onSubmitPredict)
            }
            
        }
        
        List {
            let formattedTime = String(format: "%02d:%02d", fixture.localKickoffTime.hour, fixture.localKickoffTime.minute)
            Section(header: Text("Info"), content: {
                InfoRowView(label: "Date", value:fixture.localKickoffTime.date.description())
                InfoRowView(label: "Kick-off Time", value: formattedTime)
                
            })
        }

    }
}



struct PredictionView: View {
    @State private var homeTeamPrediction = ""
    @State private var awayTeamPrediction = ""

    var gameFixture: GameFixture
    var onSubmitPredict: (Prediction) -> Void

    var body: some View {
        let isNotPredicted = !gameFixture.isPredicted
        
        VStack(spacing: 16) {
            LinearGradientText(isNotPredicted: isNotPredicted)

            HStack {
                ScoreTextField(value: $homeTeamPrediction)
                Spacer().frame(width: 16)
                ScoreTextField(value: $awayTeamPrediction)
            }

            if isNotPredicted {
                SubmitButton(onSubmit: {
                    onSubmitPredict(Prediction(id: nil,fixtureId: gameFixture.id, homeScores: homeTeamPrediction, awayScores: awayTeamPrediction))
                })
            } else {
                Text(gameFixture.getPredictionMessage())
                    .font(.body)
                    .padding(.top, 8)
            }
        }
        .padding(8)
    }
}

struct LinearGradientText: View {
    let isNotPredicted: Bool

    var body: some View {
        let title = isNotPredicted ? "Make Your Predictions" : "Predictions Submitted"
        return ZStack {
            LinearGradient(gradient: Gradient(stops: [
                .init(color: Color(red: 0.77, green: 0.66, blue: 1.00), location: 0),
                .init(color: Color(red: 0.49, green: 0.26, blue: 0.64), location: 1)
            ]), startPoint: .top, endPoint: .bottom)

            Text(title)
                .font(.title)
                .fontWeight(.bold)
                .foregroundColor(.white)
                .padding(12)
        }
        .frame(maxWidth: .infinity)
    }
}

struct ScoreTextField: View {
    @Binding var value: String

    var body: some View {
        TextField("", text: $value)
            .font(.system(size: 34))
            .foregroundColor(.black)
            .multilineTextAlignment(.center)
            .keyboardType(.numberPad)
            .padding(8)
            .background(Color.gray.opacity(0.3))
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .frame(width: 120, height: 60)
            .padding(.horizontal, 16)
    }
}

struct SubmitButton: View {
    var onSubmit: () -> Void

    var body: some View {
        Button(action: onSubmit) {
            Text("Submit Predictions")
                .fontWeight(.bold)
                .frame(width: 200, height: 40)
                .background(Color.blue)
                .foregroundColor(.white)
                .clipShape(RoundedRectangle(cornerRadius: 10))
        }
    }
}





