package com.pxw.pojo.GA;

import com.pxw.pojo.*;

import java.util.*;

/**
 * Multi-objective fitness calculator
 * Calculates hard constraint penalties, soft constraint scores and resource utilization
 */
public class FitnessCalculator {

    // Weight configuration
    private static final double W_HARD = 100.0;      // Hard constraint weight
    private static final double W_SOFT = 1.0;        // Soft constraint weight
    private static final double W_RESOURCE = 0.1;   // Resource utilization weight

    // Hard constraint penalty weights
    private static final double PENALTY_CLASS_TIME = 10.0;
    private static final double PENALTY_TEACHER_TIME = 10.0;
    private static final double PENALTY_ROOM_TIME = 10.0;
    private static final double PENALTY_CAPACITY = 5.0;
    private static final double PENALTY_TYPE_MISMATCH = 5.0;

    // Soft constraint score weights
    private static final double SCORE_TEACHER_PREFERENCE = 15.0;
    private static final double SCORE_COURSE_CONCENTRATION = 10.0;
    private static final double SCORE_ROOM_BALANCE = 5.0;
    private static final double SCORE_DAY_CONCENTRATION = 8.0;
    private static final double SCORE_ROOM_TYPE_MATCH = 10.0;

    private Bootstrap bootstrap;

    public FitnessCalculator(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Calculate individual fitness
     * Fitness = w_h * H(x) + w_s * S(x) + w_r * R(x)
     */
    public double calcFitness(Individual indiv) {
        // Create timetable
        Bootstrap threadBootstrap = new Bootstrap(bootstrap);
        threadBootstrap.createTable(indiv);

        // Calculate hard constraint penalty
        double hardPenalty = calcHardConstraintPenalty(threadBootstrap);

        // Calculate soft constraint score
        double softScore = calcSoftConstraintScore(threadBootstrap);

        // Calculate resource utilization
        double resourceUtil = calcResourceUtilization(threadBootstrap);

        // Hard constraint priority: fitness considers soft constraints only when penalty is 0
        double fitness;
        if (hardPenalty == 0) {
            fitness = W_HARD + softScore * W_SOFT + resourceUtil * W_RESOURCE * 10;
        } else {
            // Penalty term as denominator: the more hard constraints are satisfied, the higher the fitness
            fitness = W_HARD / (hardPenalty + 1);
        }

        indiv.setFitness(fitness);
        indiv.setHardPenalty(hardPenalty);
        indiv.setSoftScore(softScore);
        indiv.setResourceUtil(resourceUtil);

        return fitness;
    }

    /**
     * Calculate hard constraint penalty
     */
    public double calcHardConstraintPenalty(Bootstrap bs) {
        double penalty = 0;

        CourseTable[] tables = bs.getTables();
        if (tables == null || tables.length == 0) {
            return Double.MAX_VALUE;
        }

        // 1. Class time conflicts
        penalty += calcClassTimeClashes(tables) * PENALTY_CLASS_TIME;

        // 2. Teacher time conflicts
        penalty += calcTeacherTimeClashes(tables) * PENALTY_TEACHER_TIME;

        // 3. Room time conflicts
        penalty += calcRoomTimeClashes(tables) * PENALTY_ROOM_TIME;

        // 4. Capacity constraint violations
        penalty += calcCapacityViolations(bs) * PENALTY_CAPACITY;

        // 5. Room type mismatches
        penalty += calcTypeMismatches(bs) * PENALTY_TYPE_MISMATCH;

        return penalty;
    }

    /**
     * Calculate class time conflict count
     */
    private int calcClassTimeClashes(CourseTable[] tables) {
        int clashes = 0;
        for (int i = 0; i < tables.length; i++) {
            for (int j = i + 1; j < tables.length; j++) {
                Task taskA = tables[i].getTask();
                Task taskB = tables[j].getTask();

                if (taskA == null || taskB == null) continue;

                Cclasses classA = taskA.getCclasses();
                Cclasses classB = taskB.getCclasses();

                if (classA == null || classB == null) continue;

                // Check if classes have overlap
                if (hasClassOverlap(classA.getClassesName(), classB.getClassesName())) {
                    // Check if time conflicts
                    if (tables[i].getTimeslot().getId().equals(tables[j].getTimeslot().getId())) {
                        clashes++;
                    }
                }
            }
        }
        return clashes;
    }

    /**
     * Check if two classes have overlap
     */
    private boolean hasClassOverlap(String nameA, String nameB) {
        if (nameA == null || nameB == null) return false;
        String[] namesA = nameA.split(",");
        for (String a : namesA) {
            if (nameB.contains(a.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate teacher time conflict count
     */
    private int calcTeacherTimeClashes(CourseTable[] tables) {
        int clashes = 0;
        Map<Integer, Set<Integer>> teacherTimeMap = new HashMap<>();

        for (CourseTable table : tables) {
            if (table.getTask() == null || table.getTask().getTeacher() == null) continue;

            int teacherId = table.getTask().getTeacher().getId();
            int timeslotId = table.getTimeslot().getId();

            Set<Integer> timeslots = teacherTimeMap.computeIfAbsent(teacherId, k -> new HashSet<>());
            if (timeslots.contains(timeslotId)) {
                clashes++;
            } else {
                timeslots.add(timeslotId);
            }
        }
        return clashes;
    }

    /**
     * Calculate room time conflict count
     */
    private int calcRoomTimeClashes(CourseTable[] tables) {
        int clashes = 0;
        Map<Integer, Set<Integer>> roomTimeMap = new HashMap<>();

        for (CourseTable table : tables) {
            if (table.getRoom() == null || table.getTimeslot() == null) continue;

            int roomId = table.getRoom().getId();
            int timeslotId = table.getTimeslot().getId();

            Set<Integer> timeslots = roomTimeMap.computeIfAbsent(roomId, k -> new HashSet<>());
            if (timeslots.contains(timeslotId)) {
                clashes++;
            } else {
                timeslots.add(timeslotId);
            }
        }
        return clashes;
    }

    /**
     * Calculate capacity constraint violation count
     */
    private int calcCapacityViolations(Bootstrap bs) {
        int violations = 0;
        CourseTable[] tables = bs.getTables();

        for (CourseTable table : tables) {
            if (table.getTask() == null || table.getRoom() == null) continue;

            // Get class and room information
            Cclasses cclass = table.getTask().getCclasses();
            if (cclass == null) continue;

            int classSize = cclass.getSize();

            // Get room capacity, handle possible null
            Room room = bs.getRoom(table.getRoom().getId());
            if (room == null) {
                // Room does not exist in database, count as violation
                violations++;
                continue;
            }

            int roomCapacity = room.getCapacity();

            if (roomCapacity < classSize) {
                violations++;
            }
        }
        return violations;
    }

    /**
     * Calculate type mismatch count
     */
    private int calcTypeMismatches(Bootstrap bs) {
        // Simplified implementation: can extend room type matching logic if needed
        // Currently returns 0 because room type table may not exist in database
        return 0;
    }

    /**
     * Calculate soft constraint score
     */
    public double calcSoftConstraintScore(Bootstrap bs) {
        double score = 0;

        CourseTable[] tables = bs.getTables();
        if (tables == null || tables.length == 0) {
            return 0;
        }

        // 1. Teacher time preference score
        score += calcTeacherPreferenceScore(tables) * SCORE_TEACHER_PREFERENCE;

        // 2. Course concentration score
        score += calcCourseConcentration(tables) * SCORE_COURSE_CONCENTRATION;

        // 3. Room balance utilization score
        score += calcRoomBalance(tables) * SCORE_ROOM_BALANCE;

        // 4. Day concentration score
        score += calcDayConcentration(tables) * SCORE_DAY_CONCENTRATION;

        // 5. Room type matching score
        score += calcRoomTypeMatchScore(tables) * SCORE_ROOM_TYPE_MATCH;

        return score;
    }

    /**
     * Calculate teacher time preference score
     */
    private double calcTeacherPreferenceScore(CourseTable[] tables) {
        // Simplified implementation: return middle value as baseline
        // In real application, need to query teacher preference table
        return 0.5;
    }

    /**
     * Calculate course concentration (same course should be arranged in adjacent time slots)
     */
    private double calcCourseConcentration(CourseTable[] tables) {
        Map<Integer, List<Integer>> courseTimes = new HashMap<>();

        for (CourseTable table : tables) {
            if (table.getTask() == null || table.getTask().getCclasses() == null ||
                table.getTask().getCclasses().getCourse() == null) continue;

            int courseId = table.getTask().getCclasses().getCourse().getId();
            int timeslotId = table.getTimeslot().getId();

            courseTimes.computeIfAbsent(courseId, k -> new ArrayList<>()).add(timeslotId);
        }

        double totalScore = 0;
        for (List<Integer> times : courseTimes.values()) {
            if (times.size() <= 1) continue;

            Collections.sort(times);
            int adjacentCount = 0;
            for (int i = 0; i < times.size() - 1; i++) {
                if (times.get(i + 1) - times.get(i) <= 5) { // Adjacent time slots (5 time slots per day)
                    adjacentCount++;
                }
            }
            totalScore += (double) adjacentCount / (times.size() - 1);
        }

        return courseTimes.isEmpty() ? 0 : totalScore / courseTimes.size();
    }

    /**
     * Calculate room balance utilization
     */
    private double calcRoomBalance(CourseTable[] tables) {
        Map<Integer, Integer> roomUsage = new HashMap<>();

        for (CourseTable table : tables) {
            if (table.getRoom() == null) continue;
            roomUsage.merge(table.getRoom().getId(), 1, Integer::sum);
        }

        if (roomUsage.isEmpty()) return 0;

        // Calculate standard deviation
        double mean = roomUsage.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        double variance = roomUsage.values().stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average().orElse(0);
        double std = Math.sqrt(variance);

        // Smaller standard deviation means higher balance
        return 1.0 / (std + 1);
    }

    /**
     * Calculate day concentration (courses for same class should be concentrated in fewer days)
     */
    private double calcDayConcentration(CourseTable[] tables) {
        Map<String, Set<Integer>> classDays = new HashMap<>();

        for (CourseTable table : tables) {
            if (table.getTask() == null || table.getTask().getCclasses() == null) continue;

            String className = table.getTask().getCclasses().getClassesName();
            int timeslotId = table.getTimeslot().getId();
            int day = (timeslotId - 1) / 5 + 1; // Convert to day of week

            classDays.computeIfAbsent(className, k -> new HashSet<>()).add(day);
        }

        double totalScore = 0;
        for (Set<Integer> days : classDays.values()) {
            // Fewer days used means higher concentration
            totalScore += 1.0 / days.size();
        }

        return classDays.isEmpty() ? 0 : totalScore / classDays.size();
    }

    /**
     * Calculate room type matching score
     */
    private double calcRoomTypeMatchScore(CourseTable[] tables) {
        // Simplified implementation: return middle value
        // In real application, need to compare course type and room type
        return 0.5;
    }

    /**
     * Calculate resource utilization
     */
    public double calcResourceUtilization(Bootstrap bs) {
        double roomUtil = calcRoomUtilization(bs);
        double timeUtil = calcTimeUtilization(bs);
        return (roomUtil + timeUtil) / 2;
    }

    /**
     * Calculate room utilization
     */
    private double calcRoomUtilization(Bootstrap bs) {
        CourseTable[] tables = bs.getTables();
        if (tables == null || tables.length == 0) return 0;

        Map<Integer, Integer> roomUsage = new HashMap<>();
        for (CourseTable table : tables) {
            if (table.getRoom() != null) {
                roomUsage.merge(table.getRoom().getId(), 1, Integer::sum);
            }
        }

        int totalRooms = bs.getRooms().size();
        if (totalRooms == 0) return 0;

        double avgUsage = roomUsage.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        return avgUsage / totalRooms;
    }

    /**
     * Calculate timeslot utilization
     */
    private double calcTimeUtilization(Bootstrap bs) {
        CourseTable[] tables = bs.getTables();
        if (tables == null || tables.length == 0) return 0;

        Set<Integer> usedTimeslots = new HashSet<>();
        for (CourseTable table : tables) {
            if (table.getTimeslot() != null) {
                usedTimeslots.add(table.getTimeslot().getId());
            }
        }

        int totalTimeslots = bs.getTimeslots().size();
        if (totalTimeslots == 0) return 0;

        return (double) usedTimeslots.size() / totalTimeslots;
    }

    /**
     * Get constraint conflict details (for debugging and analysis)
     */
    public Map<String, Object> getConstraintDetails(Individual indiv) {
        Bootstrap threadBootstrap = new Bootstrap(bootstrap);
        threadBootstrap.createTable(indiv);

        Map<String, Object> details = new HashMap<>();

        CourseTable[] tables = threadBootstrap.getTables();
        details.put("classTimeClashes", calcClassTimeClashes(tables));
        details.put("teacherTimeClashes", calcTeacherTimeClashes(tables));
        details.put("roomTimeClashes", calcRoomTimeClashes(tables));
        details.put("capacityViolations", calcCapacityViolations(threadBootstrap));
        details.put("typeMismatches", calcTypeMismatches(threadBootstrap));

        details.put("hardPenalty", calcHardConstraintPenalty(threadBootstrap));
        details.put("softScore", calcSoftConstraintScore(threadBootstrap));
        details.put("resourceUtil", calcResourceUtilization(threadBootstrap));

        return details;
    }
}
