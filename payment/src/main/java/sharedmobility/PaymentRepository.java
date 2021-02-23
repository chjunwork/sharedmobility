package sharedmobility;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long>{

    Payment findByAccessId(Long accessId);

}