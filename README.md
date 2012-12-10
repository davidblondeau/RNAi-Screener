This is my solution to the CDD siRNA code problem written in November 2008.

## Code problem

Small interfering RNAs (siRNAs) are artificial, 21- to 23nt, double-stranded RNAs with 3'  2nt overhangs (see diagram below). The base pairing is not always perfect. The mechanism of action is that a protein complex, a RISC (RNA-Induced Silencing Complex), incorporates one of the two strands (called the "guide" or "antisense" strand) preferentially over the other, and this guide strand is used to bind with target mRNAs present in the cytoplasm. The RISC complex responsible for the strong siRNA-mediated knockdown effect is called the catalytic endonuclease-containing complex. When an mRNA hybridizes with the guide strand, the catalytic RISC complex cleaves the mRNA opposite the junction between 10th and 11th nucleotides (starting from the 5' end) of the guide strand. The mRNA fragments are then digested by cellular nucleases, preventing translation into a protein. This is an important post-transcriptional form of gene expression regulation.

For the purposes of this problem assume that the sense and antisense strands of the siRNA are perfect complements without any unusual base pairing. 

**1) There are various heuristic criteria used to select 21nt sequences to use as siRNAs for a given mRNA. From Ui-Tei et al. Nucelic Acids Res., 32, 936-948:**

1.  A/U at the 5' end of the antisense strand;
2.  G/C at the 5' end of the sense strand;
3.  AU-richness in the 5' terminal one-third of the antisense strand; and
4.  the absence of any GC stretch over 9bp in length.


![siRNA diagram](https://raw.github.com/davidblondeau/RNAi-Screener/master/diagram.png)

(diagram taken from siDirect: http://alps3.gi.k.u-tokyo.ac.jp/~yamada/sidirect2/index.php)

For part 1 of this problem, write a command-line application to find 21nt subsequences of a given mRNA that satisfy these four criteria.

For example, for "aauguacguacugacugacugacgcguacguacugaucg" it should find:

    *  1: auguacguacugacugacuga
    * 16: gacugacgcguacguacugau

(that's the offset from the 5' end of the mRNA and the 21nt sequence that matches with the antisense strand of the siRNA, hence rule #2 is satisfied for the first sequence because the sense strand starts at the third base (the first two hybridize with the overhang on the antisense strand). I know that's totally weird, but hopefully you get the idea. Feel free to make the output make more sense. : )

Please submit the code at this point.

**2) Now use the software you've developed above to find sets of siRNAs for each of a set of target mRNAs, so that you can create an RNAi screening library.**

For each mRNA you should pick four good candidate siRNAs (if possible) that do not hybridize well with any other mRNA in the target set (define good hybridization as including up to two mismatches over the length of the siRNA). If this is not possible, report this.

I'm not looking for an efficient algorithm here, I am more interested in how you structure the code. Feel free to read the target mRNAs from a file, or from the database, and to store results in a file, or in the database, as you wish.