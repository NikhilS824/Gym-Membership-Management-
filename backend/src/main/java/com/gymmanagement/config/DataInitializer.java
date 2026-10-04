package com.gymmanagement.config;

import com.gymmanagement.entity.*;
import com.gymmanagement.enums.*;
import com.gymmanagement.repository.*;
import com.gymmanagement.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedTrainers();
        seedPlans();
        seedMembersAndMemberships();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@gymmanagement.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            User staff = User.builder()
                    .username("staff")
                    .email("staff@gymmanagement.com")
                    .password(passwordEncoder.encode("staff123"))
                    .fullName("Front Desk Staff")
                    .role(Role.STAFF)
                    .active(true)
                    .build();

            userRepository.saveAll(List.of(admin, staff));
            System.out.println(">>> Seeded default ADMIN (admin/admin123) and STAFF (staff/staff123)");
        }
    }

    private void seedTrainers() {
        if (trainerRepository.count() == 0) {
            Trainer t1 = Trainer.builder()
                    .name("Arnold Schwarzenegger")
                    .phone("+1 555-0191")
                    .email("arnold@gym.com")
                    .specialization("Bodybuilding & Hypertrophy")
                    .experience(12)
                    .joiningDate(LocalDate.now().minusYears(3))
                    .status(PlanStatus.ACTIVE)
                    .build();

            Trainer t2 = Trainer.builder()
                    .name("Ronnie Coleman")
                    .phone("+1 555-0192")
                    .email("ronnie@gym.com")
                    .specialization("Powerlifting & Strength Training")
                    .experience(15)
                    .joiningDate(LocalDate.now().minusYears(2))
                    .status(PlanStatus.ACTIVE)
                    .build();

            Trainer t3 = Trainer.builder()
                    .name("Jay Cutler")
                    .phone("+1 555-0193")
                    .email("jay@gym.com")
                    .specialization("Cardio & Weight Loss")
                    .experience(8)
                    .joiningDate(LocalDate.now().minusYears(1))
                    .status(PlanStatus.ACTIVE)
                    .build();

            trainerRepository.saveAll(List.of(t1, t2, t3));
            System.out.println(">>> Seeded default trainers");
        }
    }

    private void seedPlans() {
        if (planRepository.count() == 0) {
            MembershipPlan p1 = MembershipPlan.builder()
                    .name("Monthly Standard")
                    .durationValue(1)
                    .durationUnit(DurationUnit.MONTH)
                    .price(new BigDecimal("50.00"))
                    .description("Access to gym equipment and locker rooms")
                    .benefits("Gym Access, Locker Room, Free WiFi")
                    .status(PlanStatus.ACTIVE)
                    .build();

            MembershipPlan p2 = MembershipPlan.builder()
                    .name("Quarterly Pro")
                    .durationValue(3)
                    .durationUnit(DurationUnit.MONTH)
                    .price(new BigDecimal("135.00"))
                    .description("10% discount for 3 months commitment")
                    .benefits("Gym Access, Locker Room, Free WiFi, 1 Free Personal Training Session")
                    .status(PlanStatus.ACTIVE)
                    .build();

            MembershipPlan p3 = MembershipPlan.builder()
                    .name("Half-Yearly Elite")
                    .durationValue(6)
                    .durationUnit(DurationUnit.MONTH)
                    .price(new BigDecimal("240.00"))
                    .description("Best value for 6 months transformation")
                    .benefits("Gym Access, Locker Room, WiFi, Sauna Access, 3 Trainer Sessions")
                    .status(PlanStatus.ACTIVE)
                    .build();

            MembershipPlan p4 = MembershipPlan.builder()
                    .name("Annual VIP Pass")
                    .durationValue(1)
                    .durationUnit(DurationUnit.YEAR)
                    .price(new BigDecimal("420.00"))
                    .description("Full year all-inclusive premium membership")
                    .benefits("24/7 Access, All Group Classes, Sauna, Unlimited Guest Passes, Dedicated Trainer")
                    .status(PlanStatus.ACTIVE)
                    .build();

            planRepository.saveAll(List.of(p1, p2, p3, p4));
            System.out.println(">>> Seeded default membership plans");
        }
    }

    private void seedMembersAndMemberships() {
        if (memberRepository.count() == 0) {
            List<Trainer> trainers = trainerRepository.findAll();
            List<MembershipPlan> plans = planRepository.findAll();

            Trainer t1 = trainers.isEmpty() ? null : trainers.get(0);
            Trainer t2 = trainers.size() > 1 ? trainers.get(1) : t1;

            MembershipPlan monthly = plans.get(0);
            MembershipPlan quarterly = plans.get(1);
            MembershipPlan halfYear = plans.get(2);
            MembershipPlan annual = plans.get(3);

            LocalDate today = LocalDate.now();

            // Member 1: Active Annual Membership
            Member m1 = Member.builder()
                    .memberId("GM00001")
                    .fullName("Alex Mercer")
                    .phone("+1 555-1001")
                    .email("alex.mercer@gmail.com")
                    .dateOfBirth(LocalDate.of(1995, 4, 12))
                    .age(31)
                    .gender("Male")
                    .address("104 Broadway St, New York")
                    .emergencyContactName("Sarah Mercer")
                    .emergencyContactPhone("+1 555-9001")
                    .joiningDate(today.minusMonths(2))
                    .status(MemberStatus.ACTIVE)
                    .trainer(t1)
                    .build();
            m1 = memberRepository.save(m1);

            Membership ms1 = createAndSaveMembership(m1, annual, today.minusMonths(2), PaymentMethod.CARD);

            // Member 2: Expiring Soon (in 4 days)
            Member m2 = Member.builder()
                    .memberId("GM00002")
                    .fullName("Jessica Alba")
                    .phone("+1 555-1002")
                    .email("jessica.alba@gmail.com")
                    .dateOfBirth(LocalDate.of(1998, 8, 24))
                    .age(28)
                    .gender("Female")
                    .address("45 Palm Drive, Miami")
                    .emergencyContactName("Mark Alba")
                    .emergencyContactPhone("+1 555-9002")
                    .joiningDate(today.minusMonths(1).minusDays(26))
                    .status(MemberStatus.ACTIVE)
                    .trainer(t2)
                    .build();
            m2 = memberRepository.save(m2);

            Membership ms2 = createAndSaveMembership(m2, monthly, today.minusMonths(1).plusDays(5), PaymentMethod.UPI);

            // Member 3: Expired 5 days ago
            Member m3 = Member.builder()
                    .memberId("GM00003")
                    .fullName("David Miller")
                    .phone("+1 555-1003")
                    .email("david.miller@gmail.com")
                    .dateOfBirth(LocalDate.of(1990, 11, 5))
                    .age(35)
                    .gender("Male")
                    .address("77 Sunset Blvd, Los Angeles")
                    .emergencyContactName("Laura Miller")
                    .emergencyContactPhone("+1 555-9003")
                    .joiningDate(today.minusMonths(4))
                    .status(MemberStatus.ACTIVE)
                    .trainer(t1)
                    .build();
            m3 = memberRepository.save(m3);

            Membership ms3 = Membership.builder()
                    .member(m3)
                    .plan(quarterly)
                    .startDate(today.minusMonths(3).minusDays(5))
                    .endDate(today.minusDays(5))
                    .status(MembershipStatus.EXPIRED)
                    .amount(quarterly.getPrice())
                    .discount(BigDecimal.ZERO)
                    .finalAmount(quarterly.getPrice())
                    .build();
            membershipRepository.save(ms3);

            paymentRepository.save(Payment.builder()
                    .member(m3)
                    .membership(ms3)
                    .amount(quarterly.getPrice())
                    .discount(BigDecimal.ZERO)
                    .finalAmount(quarterly.getPrice())
                    .paymentMethod(PaymentMethod.CASH)
                    .paymentStatus(PaymentStatus.PAID)
                    .paymentDate(LocalDateTime.now().minusMonths(3))
                    .notes("Quarterly Plan Purchase")
                    .build());

            // Member 4: Active Quarterly
            Member m4 = Member.builder()
                    .memberId("GM00004")
                    .fullName("Sophia Chen")
                    .phone("+1 555-1004")
                    .email("sophia.chen@gmail.com")
                    .dateOfBirth(LocalDate.of(2001, 2, 18))
                    .age(25)
                    .gender("Female")
                    .address("12 University Ave, Boston")
                    .emergencyContactName("Wei Chen")
                    .emergencyContactPhone("+1 555-9004")
                    .joiningDate(today.minusDays(15))
                    .status(MemberStatus.ACTIVE)
                    .trainer(t2)
                    .build();
            m4 = memberRepository.save(m4);

            Membership ms4 = createAndSaveMembership(m4, quarterly, today.minusDays(15), PaymentMethod.CARD);

            // Member 5: Active Half-Yearly
            Member m5 = Member.builder()
                    .memberId("GM00005")
                    .fullName("Marcus Aurelius")
                    .phone("+1 555-1005")
                    .email("marcus.a@gmail.com")
                    .dateOfBirth(LocalDate.of(1988, 6, 30))
                    .age(38)
                    .gender("Male")
                    .address("500 Forum Way, Rome")
                    .emergencyContactName("Faustina")
                    .emergencyContactPhone("+1 555-9005")
                    .joiningDate(today.minusMonths(1))
                    .status(MemberStatus.ACTIVE)
                    .trainer(t1)
                    .build();
            m5 = memberRepository.save(m5);

            Membership ms5 = createAndSaveMembership(m5, halfYear, today.minusMonths(1), PaymentMethod.BANK_TRANSFER);

            // Seed today's attendance for active members
            attendanceRepository.save(Attendance.builder()
                    .member(m1)
                    .checkInTime(LocalDateTime.now().minusHours(2))
                    .checkOutTime(LocalDateTime.now().minusHours(1))
                    .date(today)
                    .status("PRESENT")
                    .build());

            attendanceRepository.save(Attendance.builder()
                    .member(m4)
                    .checkInTime(LocalDateTime.now().minusMinutes(45))
                    .date(today)
                    .status("PRESENT")
                    .build());

            System.out.println(">>> Seeded initial members, memberships, payments, and attendance records");
        }
    }

    private Membership createAndSaveMembership(Member member, MembershipPlan plan, LocalDate startDate, PaymentMethod method) {
        LocalDate endDate = DateUtils.calculateEndDate(startDate, plan.getDurationValue(), plan.getDurationUnit());

        Membership membership = Membership.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(MembershipStatus.ACTIVE)
                .amount(plan.getPrice())
                .discount(BigDecimal.ZERO)
                .finalAmount(plan.getPrice())
                .build();

        Membership saved = membershipRepository.save(membership);

        Payment payment = Payment.builder()
                .member(member)
                .membership(saved)
                .amount(plan.getPrice())
                .discount(BigDecimal.ZERO)
                .finalAmount(plan.getPrice())
                .paymentMethod(method)
                .paymentStatus(PaymentStatus.PAID)
                .paymentDate(startDate.atStartOfDay())
                .notes("Plan Purchase: " + plan.getName())
                .build();

        paymentRepository.save(payment);
        return saved;
    }
}
