package sharedmobility;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long>{

    Issue findByAccessId(Long accessId);

}