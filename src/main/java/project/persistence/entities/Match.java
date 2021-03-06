package project.persistence.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match implements Comparable<Match> {
    private long id;
    private int homeTeamScore;
    private int awayTeamScore;
    private LocalDateTime matchDate;
    private int round;
    private String location;
    private Team homeTeam;
    private Team awayTeam;
    private boolean played;
    private Tournament tournament;

    public Match(){}

    public Match(Team homeTeam, Team awayTeam, int round, Tournament tournament) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.round = round;
        played = false;
        this.tournament = tournament;
    }

    @Id
    @Column(name = "MatchId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Team getHomeTeam() { return homeTeam; }
    public void setHomeTeam(Team homeTeam) { this.homeTeam = homeTeam; }

    @ManyToOne
    public Team getAwayTeam() { return awayTeam; }
    public void setAwayTeam(Team awayTeam) { this.awayTeam = awayTeam; }

    @ManyToOne
    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getPlayed() { return played; }

    public void setPlayed(boolean played) { this.played = played; }

    @Override
    public int compareTo(Match o) {
        if (this.getRound() > o.getRound()) return 1;
        else if(this.getRound() < o.getRound()) return -1;
        else {
            if(this.getMatchDate() != null && o.getMatchDate() != null)
                return this.getMatchDate().compareTo(o.getMatchDate());
        }
        return 0;
    }
}
