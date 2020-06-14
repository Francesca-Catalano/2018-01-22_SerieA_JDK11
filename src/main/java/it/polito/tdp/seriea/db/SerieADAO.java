package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasondAndPoints;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public void listAllSeasons(Map<Integer,Season> map) {
		String sql = "SELECT season, description FROM seasons";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				map.put(res.getInt("season"),new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	
	public List<SeasondAndPoints> listSeasondAndPoints(String team,Map<Integer,Season> map) {
		String sql = "select distinct  tableH.Season,SUM(tableH.tot + tableA.tot+ tableD.tot) as sum " + 
				"from " + 
				"(select Season,HomeTeam,count(distinct(match_id))*3 as tot " + 
				"from matches " + 
				"where HomeTeam=? and FTR='H'" + 
				"group by Season,HomeTeam) as tableH , (select Season,HomeTeam,count(distinct(match_id))*3 as tot\n" + 
				"from matches " + 
				" where AwayTeam=? and FTR='A' " + 
				"group by Season,HomeTeam) as tableA,(select Season,HomeTeam,count(distinct(match_id)) as tot " + 
				"from matches " + 
				" where AwayTeam=? OR HomeTeam=? and FTR='D' " + 
				"group by Season,HomeTeam) as tableD " + 
				"where tableH.Season= tableA.Season " + 
				"group by  tableH.Season ";
		List<SeasondAndPoints> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team);
			st.setString(2, team);
			st.setString(3, team);
			st.setString(4, team);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new SeasondAndPoints(map.get(res.getInt("tableH.Season")), res.getInt("sum")) );
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

