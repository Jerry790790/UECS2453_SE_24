package edu.assignment.dataStructure;

import java.util.*;

/*
 * KDTree is an extension from binary search tree (BST). BST is 1 dimensional, but KDTree is 2 dimensional.
 * This allows for easy traversing among locations.
 */

public class KDTree<E extends Comparable<E>> extends BST<E> {
    private TreeNode<DeliveryJob> root;
    
    // The fixed variable for conversion of latitude/longitude degrees to kilometers.
    private static final double KM_PER_DEGREE = 111.0; 

    public void build(List<DeliveryJob> jobs) {
        this.root = buildRecursive(jobs, 0);
    }

    // recursive function that creates the tree based on DeliveryJob input.
    // depth defines the tree creation layer by layer
    private TreeNode<DeliveryJob> buildRecursive(List<DeliveryJob> jobs, int depth) {
        if (jobs == null || jobs.isEmpty()) return null;
        
        // == 0: the tree is sorted from South to North (Latitude)
        // != 0: the tree is sorted from West to East (Longitude)
        // Why sort like this: when u pick middle as parent, middle parent used as guide to determine 
        // if ur passed location is in which 4 directions. Very effective because can skip huge numbers
        // of steps when your data is big (eg. 1000 locations)
        if (depth % 2 == 0) jobs.sort(Comparator.comparingDouble(j -> j.getLat()));
        else jobs.sort(Comparator.comparingDouble(j -> j.getLon()));

        // Tree parents usually in the middle for more effective traversing, 
        // this is to use the middle value as parent
        int median = jobs.size() / 2;
        TreeNode<DeliveryJob> node = new TreeNode<>(jobs.get(median));

        node.left = buildRecursive(new ArrayList<>(jobs.subList(0, median)), depth+1);
        node.right = buildRecursive(new ArrayList<>(jobs.subList(median + 1, jobs.size())), depth+1);

        return node;
    }

    private DeliveryJob bestJob;
    private double bestDist;

    public DeliveryJob findNearest(double x, double y, Set<Integer> visited) {
        bestJob = null;
        bestDist = Double.MAX_VALUE;
        search(root, x, y, visited, 0);
        return bestJob;
    }

    private void search(TreeNode<DeliveryJob> node, double x, double y, Set<Integer> visited, int depth) {
        if (node == null) return;

        // Haversine(latitude, longitude) distance in KM
        double d = calculateHaversine(x, y, node.element.getLat(), node.element.getLon());

        if (!visited.contains(node.element.getId()) && d < bestDist) {
            bestDist = d;
            bestJob = node.element;
        }
        
        // determines if current checking is for latitude/longitude
        boolean isVertical = (depth % 2 == 0);
        double diff = isVertical ? (x - node.element.getLat()) : (y - node.element.getLon());
        TreeNode<DeliveryJob> near = diff < 0 ? node.left : node.right;
        TreeNode<DeliveryJob> far = diff < 0 ? node.right : node.left;

        search(near, x, y, visited, depth+1);

        // Pruning: If perpendicular distance to the split line (in KM) 
        // is greater than our current best, don't need to check the other side
        if (Math.abs(diff) * KM_PER_DEGREE < bestDist) {
            search(far, x, y, visited, depth+1);
        }
    }

    // This is to convert the latitude/longitude format into kilometers.
    public static double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; 
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}