package HYU.FishShip.Core.Repository;

import HYU.FishShip.Core.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    // 필터링된 게시물 조회 (채용공고, 인턴공고, 대외활동 등)
    @Query("SELECT p FROM Posts p WHERE " +
            "(COALESCE(:category, '') = '' OR p.category = :category) AND " +
            "(COALESCE(:jobTitle, '') = '' OR p.jobTitle LIKE %:jobTitle%) AND " +
            "(COALESCE(:experience, -1) = -1 OR p.experience = :experience) AND " +
            "(COALESCE(:education, '') = '' OR p.education LIKE %:education%) AND " +
            "(COALESCE(:activityField, '') = '' OR p.activityField LIKE %:activityField%) AND " +
            "(COALESCE(:activityDuration, -1) = -1 OR p.activityDuration = :activityDuration) AND " +
            "(COALESCE(:hostingOrganization, '') = '' OR p.hostingOrganization LIKE %:hostingOrganization%) AND " +
            "(COALESCE(:onlineOrOffline, '') = '' OR p.onlineOrOffline LIKE %:onlineOrOffline%) AND " +
            "(COALESCE(:targetAudience, '') = '' OR p.targetAudience LIKE %:targetAudience%) AND " +
            "(COALESCE(:contestBenefits, '') = '' OR p.contestBenefits LIKE %:contestBenefits%) AND" +
            "(COALESCE(:location, '') = '' OR p.location LIKE %:location%)")
    List<Posts> findByFilters(
            @Param("category") String category,
            @Param("jobTitle") String jobTitle,
            @Param("experience") Integer experience,
            @Param("education") String education,
            @Param("activityField") String activityField,
            @Param("activityDuration") Integer activityDuration,
            @Param("hostingOrganization") String hostingOrganization,
            @Param("onlineOrOffline") String onlineOrOffline,
            @Param("targetAudience") String targetAudience,
            @Param("contestBenefits") String contestBenefits,
            @Param("location") String location
    );

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Posts> findByKeyword(@Param("keyword") String keyword);

}