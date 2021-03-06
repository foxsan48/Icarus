package org.alexdev.icarus.dao.mysql.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.alexdev.icarus.game.player.PlayerDetails;
import org.alexdev.icarus.game.player.PlayerManager;
import org.alexdev.icarus.dao.mysql.Dao;
import org.alexdev.icarus.dao.mysql.Storage;
import org.alexdev.icarus.game.moderation.Permission;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.log.Log;

import com.google.common.collect.Lists;

public class PlayerDao {
    
    public static PlayerDetails getDetails(int userId) {

        PlayerDetails details = new PlayerDetails(null);
        Player player = PlayerManager.getById(userId);

        if (player != null) {
            details = player.getDetails();
        } else {

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {

                sqlConnection = Dao.getStorage().getConnection();
                
                preparedStatement = Dao.getStorage().prepare("SELECT * FROM users WHERE id = ? LIMIT 1", sqlConnection);
                preparedStatement.setInt(1, userId);
                
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    fill(details, resultSet);
                }

            } catch (Exception e) {
                Log.exception(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return details;
    }

    public static boolean login(Player player, String ssoTicket) {
        
        boolean success = false;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Dao.getStorage().getConnection();
            preparedStatement = Dao.getStorage().prepare("SELECT * FROM users WHERE sso_ticket = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, ssoTicket);
            
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                fill(player.getDetails(), resultSet);
                success = true;
            }

        } catch (Exception e) {
            Log.exception(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return success;
    }

    public static int getId(String username) {

        int id = -1;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Dao.getStorage().getConnection();
            preparedStatement = Dao.getStorage().prepare("SELECT id FROM users WHERE username = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, username);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (Exception e) {
            Log.exception(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return id;    
    }
    
    public static String getName(int id) {

        String name = null;
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Dao.getStorage().getConnection();
            preparedStatement = Dao.getStorage().prepare("SELECT username FROM users WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, id);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                name = resultSet.getString("username");
            }
            
        } catch (Exception e) {
            Log.exception(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return name;    
    }

    public static void save(PlayerDetails details) {
        
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Dao.getStorage().getConnection();
            preparedStatement = Dao.getStorage().prepare("UPDATE users SET mission = ?, figure = ?, gender = ?, rank = ?, credits = ?, home_room = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, details.getMission());
            preparedStatement.setString(2, details.getFigure());
            preparedStatement.setString(3, details.getGender());
            preparedStatement.setInt(4, details.getRank());
            preparedStatement.setInt(5, details.getCredits());
            preparedStatement.setInt(6, details.getHomeRoomId());
            preparedStatement.setInt(7, details.getId());
            preparedStatement.execute();

        } catch (Exception e) {
            Log.exception(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
    
    public static List<Permission> getPermissions() {
        List<Permission> permissions = Lists.newArrayList();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Dao.getStorage().getConnection();
            preparedStatement = Dao.getStorage().prepare("SELECT * FROM users_permissions", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                
                int rank = resultSet.getInt("rank");
                String permission = resultSet.getString("permission");
                boolean inheritable = resultSet.getByte("inheritable") == 1;
                
                permissions.add(new Permission(permission, inheritable, rank));
            }

        } catch (Exception e) {
            Log.exception(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return permissions;
    }
        
    public static PlayerDetails fill(PlayerDetails details, ResultSet row) throws SQLException {
        details.fill(row.getInt("id"), row.getString("username"), row.getString("mission"),  row.getString("figure"), row.getString("gender"), row.getInt("rank"), row.getInt("credits"), row.getInt("home_room"));
        return details;
    }


}
