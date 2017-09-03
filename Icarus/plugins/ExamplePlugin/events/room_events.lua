--[[
	Room request enter event called when the user selects a room to enter
	
	param: 
			Player 	- person who wants to enter
			Room 	- the room want to enter
			
	return: Boolean - event cancelled state
--]]
function onRoomRequestEvent(player, room)
	log:println("Request room event called")
	
	-- Force the person to enter their own room when they try to request other rooms
	-- player:getRoomUser():setRequestedRoomId(player:getRooms():get(0):getData():getId())
	
	return false
end

--[[
	Room enter event called when the user has entered a room
	
	param: 
			Player 	- person who entered room
			Room 	- the room they entered
			
	return: Boolean - event cancelled state
--]]
function onRoomEnterEvent(player, room)
	log:println("Room enter event called")
	return false
end

--[[
	Room leave event called when the user has left a room
	
	param: 
			Player 	- person who left room
			Room 	- the room they left
			
	return: Boolean - event cancelled state
--]]
function onRoomLeaveEvent(player, room)
	log:println("Room leave event called")
	return false
end