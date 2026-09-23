package com.example.GameService.repositories;

import com.example.GameService.entities.RoomModel;
import com.example.GameService.repositories.projections.RoomProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, Long> {


    @Query("""
        SELECT
            r.id AS id,
            r.name AS name,
            COUNT(rp.id) AS playersCount,
            r.maxPlayers AS maxPlayers,
            r.status AS status
        FROM RoomModel r
        LEFT JOIN RoomPlayerModel rp
            ON rp.roomId = r.id
        GROUP BY
            r.id,
            r.name,
            r.maxPlayers,
            r.status
    """)
    List<RoomProjection> getRooms();

    @Query("""
        SELECT
            r.id AS id,
            r.name AS name,
            COUNT(rp.id) AS playersCount,
            r.maxPlayers AS maxPlayers,
            r.status AS status
        FROM RoomModel r
        LEFT JOIN RoomPlayerModel rp
            ON rp.roomId = r.id
        WHERE r.id = :roomId
        GROUP BY
            r.id,
            r.name,
            r.maxPlayers,
            r.status
    """)
    Optional<RoomProjection> getRoomById(@Param("roomId") Long roomId);
}
