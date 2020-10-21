#include <iostream>
#include <cstring>
#include <vector>
#include <map>
#include <random>
#include <algorithm>
using namespace std;

constexpr int MAX_ERRORS = 2;

int overlapLength(const string & s1, const string & s2) noexcept
{
    for (int i = 0, n = 1 + s1.size() - 12 ; i < n; ++i)
    {
        int errors = 0;
        for(int j = 0, p = s1.size() - i; j < p && errors <= MAX_ERRORS; ++j)
            if(s1[i + j] != s2[j]) ++errors;

        if(errors <= MAX_ERRORS) return s1.size() - i;
    }
    return 0;
}

char get_Most_Freq_Char(const vector<char> allChars)
{
    map<char, int> counts_per_char;

    for(auto c : allChars) ++counts_per_char[c];

    pair<char, int> max_pair = *counts_per_char.begin();

    for(auto each : counts_per_char) {
        if (each.second > max_pair.second)
            max_pair = each;
    }
    return max_pair.first;
}

string assemble_genome_error_prone(vector<string> reads) noexcept
{
    string result_genome;
    result_genome.reserve(1000);
    result_genome += reads.front();

    string first_read = reads.front(), cur_read;
    int cur_id = 0;

    while(reads.size() > 1)
    {
        cur_read = move(reads[cur_id]);
        reads.erase(reads.begin() + cur_id);

        vector<int> overlapLengths;
        vector<int> pos;

        for(int i = 0; i < reads.size(); ++i)
        {
            int overlap = overlapLength(cur_read, reads[i]);
            if(overlapLengths.empty() || overlap >= overlapLengths.back())
            {
                overlapLengths.push_back(overlap);
                pos.push_back(i);
                cur_id = i;
            }
        }

        if(overlapLengths.size() > 3)
        {
            char* suffix =  &result_genome[result_genome.size() - overlapLengths[overlapLengths.size() - 4]];
            char* prefix1 = &reads[pos[pos.size() - 4]][0];
            char* prefix2 = &reads[pos[pos.size() - 3]][
                    (overlapLengths[overlapLengths.size() - 3] - overlapLengths[overlapLengths.size() - 4])];
            char* prefix3 = &reads[pos[pos.size() - 2]][
                    (overlapLengths[overlapLengths.size() - 2] - overlapLengths[overlapLengths.size() - 4])];
            char* prefix4 = &reads[pos[pos.size() - 1]][
                    (overlapLengths[overlapLengths.size() - 1] - overlapLengths[overlapLengths.size() - 4])];

            for(int i = 0, n = overlapLengths[overlapLengths.size() - 4]; i < n; ++i,
                    ++suffix, ++prefix1, ++prefix2, ++prefix3, ++prefix4)
            {
                if(*suffix == *prefix1 && *prefix1 == *prefix2 &&
                   *prefix2 == *prefix3 && *prefix3 == *prefix4)
                    continue;

                const char c = get_Most_Freq_Char({*suffix, *prefix1, *prefix2, *prefix3, *prefix4});
                *suffix = *prefix1 = *prefix2 = *prefix3 = *prefix4 = c;
            }
        }

        result_genome += reads[cur_id].substr(overlapLengths.back());
    }

    result_genome.erase(0, overlapLength(reads[0], first_read));
    return result_genome;
}

int main ()
{
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    vector<string> reads;
    reads.reserve(5);
    string s;
    int i=0;

    while(i<5) {
        cin >> s;
        reads.emplace_back(move(s));
        i++;
    }

    random_device rd;
    mt19937 g(rd());

    shuffle(reads.begin(), reads.end(), g);

    cout << assemble_genome_error_prone(move(reads)) << endl;

    return 0;
}