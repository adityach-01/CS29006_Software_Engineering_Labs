#include <iostream>
#include <string>
#include "rapidxml.hpp"
#include "rapidxml_utils.hpp"
#include <bits/stdc++.h>

#define INF 10000;

using namespace rapidxml;
using namespace std;

xml_document<> doc;
xml_node<> *root_node = NULL;

class Node
{
public:
    string id;
    long double latitude;
    long double longitude;
    string name;
    int graph_id = -1;
    int no_ways;

    // Non parameterised constructor
    Node() {}

    // Parameterised constructor
    Node(string id, long double lat, long double lon, string n, int gid, int ways)
    {
        this->id = id;
        this->latitude = lat;
        this->longitude = lon;
        this->name = n;
        this->graph_id = gid;
        this->no_ways = ways;
    }
};

struct way_data
{
    vector<string> node_ids;
};

map<string, Node> graphNode;
int num_nodes = 0;
int num_ways = 0;
vector<struct way_data> WayData;
vector<pair<long double, string>> distanceArr;
vector<pair<long double,int>> adjList[100010];


void SearchForNode(string input)
{

    int index = 0;

    if (input.compare("-1") == 0)
    {
        index = 1;
        return;
    }

    for(auto it : graphNode){
        string s_id = it.first;
        string s_name = it.second.name;
        transform(s_name.begin(), s_name.end(), s_name.begin(), ::tolower);

        if (s_id.find(input) != std::string::npos)
        {
            // The string has been found
            cout << "<node id = " << s_id << "  lat = " << it.second.latitude << "  lon = " << it.second.longitude << "  name = " << it.second.name << ">" << endl;
            index = 1;
        }

        else if (s_name.find(input) != std::string::npos)
        {
            cout << "<node id = " << s_id << "  lat = " << it.second.latitude << "  lon = " << it.second.longitude << "  name = " << it.second.name << ">" << endl;
            index = 1;
        }
    }
    
    if(index == 0)  cout << "No such entry found !!" << endl;
}

void swap(int i, int j)
{
    pair<long double, string> temp = distanceArr[i];
    distanceArr[i] = distanceArr[j];
    distanceArr[j] = temp;
}

long double getDistance(string node1, string node2)
{

    // Converting all the latitude and longitude from degress to radians

    long double lat1 = graphNode[node1].latitude * ((M_PI) / 180);
    long double long1 = graphNode[node1].longitude * ((M_PI) / 180);
    long double lat2 = graphNode[node2].latitude * ((M_PI) / 180);
    long double long2 = graphNode[node2].longitude * ((M_PI) / 180);

    cout << setprecision(10) << fixed;

    long double d_long = long2 - long1;
    long double d_lat = lat2 - lat1;

    long double distance = pow(sin(d_lat / 2), 2) + cos(lat1) * cos(lat2) *
                                                        pow(sin(d_long / 2), 2);

    distance = 2 * asin(sqrt(distance));

    // Radius of Earth in Kilometers R = 6371
    long double Radius_earth = 6371;

    // Calculate the result, the answer is in kilometers
    distance = distance * Radius_earth;

    return distance;
}

int getIndex(string ide)
{
    return graphNode[ide].graph_id;
}

void ParseNode(xml_node<> *root_node)
{
    // Creating an array of graph nodes

    int index = 1;
    // Iterate over the node in the osm file to find number of nodes and storing the data of nodes
    for (xml_node<> *map_node = root_node->first_node("node"); map_node; map_node = map_node->next_sibling())
    {
        num_nodes++;
        string nodeid;
        long double lat;
        long double lon;

        xml_attribute<> *id_attribute = map_node->first_attribute("id");
        nodeid = id_attribute->value();

        xml_node<> *node = map_node;

        int j = 0;
        for (xml_attribute<> *attr = node->first_attribute("lat");
             attr; attr = attr->next_attribute())
        {
            std::cout << std::setprecision(8) << std::fixed;

            if (j == 0){
                lat = std::stold(attr->value());
            }
            if (j == 1){
                lon = std::stold(attr->value());
            }

            j++;
        }

        // Checking if the node has a name
        // Try using map_node->Name()
        int alpha = 0;
        string Name;


        for (xml_node<> *tag_node = map_node->first_node("tag");tag_node; tag_node = tag_node->next_sibling())
        {
            for (xml_attribute<> *attr_tag = tag_node->first_attribute();
                 attr_tag; attr_tag = attr_tag->next_attribute())
            {

                string s = attr_tag->name(); // k = "name"  k is name() and "name" is value()
                string p = attr_tag->value();

                // Checking if the tag contains name in the attribute
                if (s.compare("k") == 0)
                {
                    if (p.compare("name") == 0)
                    {
                        attr_tag = attr_tag->next_attribute();
                        Name = attr_tag->value();
                        alpha++;
                        break;
                    }
                }
            }

            if (alpha == 1)
            {
                alpha = 0;
                break;
            }
        }

        // Creating the node to store the information
        Node temp = Node(nodeid, lat, lon, Name, index, 0);

        // Adding the object in the array of objects
        graphNode[nodeid] = temp;
        index++;
    }
}

void ParseWays(xml_node<> *root_node)
{

    for (xml_node<> *way_node = root_node->first_node("way"); way_node; way_node = way_node->next_sibling())
    {

        // Iterating through the children nodes of the way elements to find the nodes that they contain
        struct way_data temp;

        for (xml_node<> *nd_node = way_node->first_node("nd"); nd_node; nd_node = nd_node->next_sibling())
        {
            // Iterating through all the nd elements of a way element and storing the node ids in a vector

            xml_attribute<> *nd_tag = nd_node->first_attribute();
            temp.node_ids.push_back(nd_tag->value());
        }

        // Pushing the way1 way data node in the vector
        WayData.push_back(temp);
        num_ways++;
    }
}

void search()
{
    string input = "";
    cout << "Enter the part of node id or node name to search for the node" << endl;
    cout << "Enter -1 to exit the process" << endl;

    // Now we have to return all the nodes which have the the given substring in the name or id
    while (input.compare("-1"))
    {
        cout << "Enter : ";
        cin >> input;

        // Function for doing the job
        SearchForNode(input);
    }
}

void getKnearest(int k)
{
    // Finding the k nearest nodes in the array
    if (k > num_nodes)
        return;

    for (int i = 0; i < k; i++)
    {
        cout << setprecision(10) << fixed;
        long double min = distanceArr[i].first;
        int cnt = i;
        int len = distanceArr.size();

        for (int j = i; j < len; j++)
        {
            if (distanceArr[j].first < min)
            {
                min = distanceArr[j].first;
                cnt = j;
            }

            // The minimum is the one with index cnt
        }

        // Swapping j with i and repeating this step for k times
        // O(nk) algorithm

        string in_id = distanceArr[cnt].second;
        SearchForNode(in_id);
        cout << distanceArr[cnt].first << endl;
        swap(i, cnt);
    }
}

void knn()
{
    cout << "Enter the node id and the number of nearest nodes that you want to check" << endl;
    cout << "Enter -1 to exit the process" << endl;

    string node_id = "";
    int k;

    while (true)
    {
        cout << "Enter id of the node : ";
        cin >> node_id;

        if (node_id.compare("-1") == 0)
        {
            break;
        }

        int index = getIndex(node_id);
        int val = 0;

        if (index == -1)
        {
            cout << "Enter the correct id of the node" << endl;
            continue;
        }

        cout << "Enter k : ";
        cin >> k;

        for (auto it : graphNode)
        {
            if (it.second.graph_id == index)
                continue;
            distanceArr.push_back(make_pair(getDistance(node_id, it.first), it.first));
        }

        getKnearest(k);
    }
}

void createGraph(){

    for(auto it : WayData){
        // it is a struct
        string prevNode = "";
        for(auto node : it.node_ids){

            int id2 = getIndex(node);
            if(id2 == -1) continue;

            if(prevNode.compare("") == 0){
                prevNode = node;
                continue;
            }

            // calc dist bw node and prevNode
            long double x = getDistance(node, prevNode);
            adjList[graphNode[node].graph_id].push_back(make_pair(x,graphNode[prevNode].graph_id));
            adjList[graphNode[prevNode].graph_id].push_back(make_pair(x,graphNode[node].graph_id));

        }
    }
}

long double dist[100010];
int visited[100010];

void dijkstra(string id){

    for (int i = 0; i < 100010; i++)
    {
        dist[i] = 1e5;
    }
    
    memset(visited, 0, sizeof(visited));

    // cout << setprecision(10) << fixed;

    priority_queue<pair<long double, int>, vector<pair<long double, int>>> pq;
    int sc = getIndex(id);
    dist[sc] = 0;

    pq.push(make_pair(0,sc));

    while(!pq.empty()){

        pair<long double, int> temp = pq.top();
        pq.pop();

        if(visited[temp.second]) continue;
        visited[temp.second] = 1;

        for(auto v : adjList[temp.second]){
            int neigh = v.second;
            long double wt = v.first;
            if(dist[neigh] > dist[temp.second] + wt){
                dist[neigh] = dist[temp.second] + wt;
                pq.push(make_pair(-dist[neigh], neigh));
            }

        }
    }
}

void shortestCalc(){
    string n1, n2;
    int id;
    int flag = 0;

    while(!flag){
        cout<<"Enter the id of 2 nodes:"<<endl;

        while(true){
            // cout<<"Node 1 : ";
            cin>>n1;
            if(n1.compare("-1") == 0){
                flag = 1;
                break;
            }

            int x = getIndex(n1);
            if(x == -1){
                cout<<"Incorrect Id, pls reenter"<<endl;
                continue;
            }

            break;
        }

        if(flag == 1) break;

        while(true){
            // cout<<"Node 2 : ";
            cin>>n2;
            id = getIndex(n2);
            if(id == -1){
                cout<<"Incorrect Id, pls reenter"<<endl;
                continue;
            }

            break;
        }

        dijkstra(n1);

        cout<<"The distance between the nodes is : ";
        cout<<dist[id]<<" "<<"km"<<endl;

    }
}



int main()
{
    cout << "Parsing my map osm file(mapkgp.xml)....." << endl;

    std::ifstream file("mapkgp.osm");
    std::stringstream buffer;
    buffer << file.rdbuf();
    file.close();
    std::string content(buffer.str());

    // Parse the buffer
    doc.parse<0>(&content[0]);

    // Find out the root node
    root_node = doc.first_node("osm");

    // parses the nodes and creates the hash map containing nodes data
    ParseNode(root_node);

    // parses the ways and stores the information
    ParseWays(root_node);

    cout << "The number of nodes in the osm file is : " << num_nodes << endl;
    cout << "The number of ways in the osm file is : " << num_ways << endl;

    // PARSING OF THE CODE IS DONE
    // NOW WE WILL IMPLEMANT THE SEARCH FEATURE
    search();

    
    // Now we have to find the k nearest nodes to a given node of the graph
    // compute the distance of the parent node from all the nodes and find the k nearest nodes to the goven node
    knn();
    
    // GRAPH CREATION
    createGraph();

    // Applying dijkstra to the graph for finding shortest path from a given node
    shortestCalc();

    return 0;
}

